package com.example.vejrapp.ui.day

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vejrapp.R
import com.example.vejrapp.data.cropBitmap
import com.example.vejrapp.data.getBitmapFromImage
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.ui.search.SearchBar
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// The page displayed at the landing screen/main screen/ today's weather
@Composable
fun Day(navController: NavHostController) {
    Column {
        SearchBar(onNextButtonClicked = { navController.navigate(Route.Settings.name) })
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            item { TopWeather() }
            item { CautionBox() }
            item { LazyRowWithCards() }
            item { DetailsBox() }
            item { ApiTimestamp() }
        }
    }
}

@Composable
fun TopWeather() {
    val dayViewModel: DayViewModel = hiltViewModel<DayViewModel>()

    val currentWeather by dayViewModel.currentWeather.collectAsState()
    val weatherImage = currentWeather.currentCondition.toString()
    val imageRes = weatherImage.mapToYRImageResource()
    val fontColor = Color.White
    val bitmap = getBitmapFromImage(LocalContext.current, imageRes)

    // Crop the transparent/whitespace areas
    val croppedBitmap = cropBitmap(bitmap)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Day timestamp
        Text(
            text = prettyDate(currentWeather.expires),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            color = fontColor
        )
        // TODO evaluate local time
//        Text(
//            text = "Local time 12:23",
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxWidth(),
//            color = fontColor
//        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Temperature block
            Column {
                //Current temperature
                Text(
                    text = "${currentWeather.currentTemperature}°",
                    fontSize = 50.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
                //Min-Max temperature
                Text(
                    text = stringResource(R.string.day_temp_range).format(
                        currentWeather.currentMinTemperature,
                        currentWeather.currentMaxTemperature
                    ),
                    fontSize = 20.sp,
                    color = fontColor,
                    modifier = Modifier
                        .padding(2.dp)
                )
                // Feels like temperature
                Text(
                    text = stringResource(R.string.day_feels_like).format(currentWeather.feelsLike),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
            }
            Spacer(modifier = Modifier.width(25.dp))

            // Weather icon
            Image(
                bitmap = croppedBitmap.asImageBitmap(),
                contentDescription = "Weather icon",
                modifier = Modifier
                    .size(175.dp)
            )
        }
    }
}

// The caution box in the weather screen to display
// caution alerts to the user (showing dummy data
//  the moment)
@Composable
fun CautionBox() {
    val fontColor = Color.White
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.day_caution),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = fontColor
            )
            Text(
                text = stringResource(R.string.day_caution_rainy_coming_days),
                color = fontColor
            )
        }
    }
}

// In the lazyrow of the hour view
// this is the composable generating
// each hour section within the hour view
@Composable
fun CardWithColumnAndRow(hour: Int) {
    val dayViewModel = hiltViewModel<DayViewModel>()

    val currentWeather by dayViewModel.currentWeather.collectAsState()
    val weatherImage = currentWeather.hourlyCondition[hour]
    val imageRes = weatherImage.mapToYRImageResource()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .width(100.dp) // Set the card's width
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Image (you can replace the URL with your image source)
            Image(
                painter = painterResource(id = imageRes), // Use your own image resource
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )

            // Second Text
            Text(
                text = "${currentWeather.hourlyTemperature[hour]}°",
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(4.dp)
                    // Try to center the text more by offsetting the degree symbol
                    .offset(x = 4.dp)
            )
            // Remove rain if no data is available
            if (currentWeather.currentPercentageRain != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    //Image (you can replace the URL with your image source)
                    Image(
                        painter = painterResource(R.drawable.baseline_umbrella_24), // Use your own image resource
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .rotate(180F)
                    )

                    // Text
                    Text(
                        text = "${currentWeather.hourlyPercentageRain[hour]}%",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Third Text
            Text(
                text = "%02d:00".format(hour),
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
    Spacer(modifier = Modifier.width(4.dp))
}

// the lazy row for the hourly view
@Composable
fun LazyRowWithCards() {
    val startHour = LocalTime.now().hour
    val hourList = List(24) { index -> (index + startHour) % 24 }
    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(6.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(hourList) { hourList -> // You can change the number of cards as needed
            CardWithColumnAndRow(hourList)
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
        }
    }
}


// The bottom section of the main screen displaying
// the additional parameters of the day
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsBox() {
    val dayViewModel = hiltViewModel<DayViewModel>()
    val currentWeather by dayViewModel.currentWeather.collectAsState()

    val fontColor = Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = stringResource(R.string.day_details),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
            color = fontColor
        )
        FlowRow(
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Detail(
                painterId = R.drawable.baseline_water_drop_24,
                text = stringResource(R.string.day_humidity),
                value = currentWeather.humidity,
                unit = currentWeather.units.relativeHumidity
            )
            Detail(
                painterId = R.drawable.baseline_air_24,
                text = stringResource(R.string.day_wind_speed),
                value = currentWeather.currentWindSpeed,
                unit = currentWeather.units.windSpeed
            )
            Detail(
                painterId = R.drawable.outline_wb_sunny_24,
                text = stringResource(R.string.day_uv_index),
                value = currentWeather.uvIndex
            )
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = stringResource(R.string.day_rain),
                value = currentWeather.currentPercentageRain,
                unit = currentWeather.units.probabilityOfPrecipitation
            )
            Detail(
                painterId = R.drawable.baseline_compress_24,
                text = stringResource(R.string.day_pressure),
                value = currentWeather.pressure,
                unit = currentWeather.units.airPressureAtSeaLevel
            )
        }
    }
}

@Composable
fun Detail(
    painterId: Int,
    rotateIcon: Boolean = false,
    text: String,
    value: Float?,
    unit: String? = ""
) {

    if (value == null) {
        return
    }

    val fontColor = Color.Black

    Column(modifier = Modifier.padding(4.dp)) {

        Icon(
            painter = painterResource(painterId),
            contentDescription = text,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .rotate(if (rotateIcon) 180F else 0F),
            tint = fontColor
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = fontColor
        )
        Text(
            text = "$value $unit",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = fontColor
        )
    }
}

@Composable
fun ApiTimestamp() {
    val dayViewModel = hiltViewModel<DayViewModel>()
    val currentWeather by dayViewModel.currentWeather.collectAsState()

    val fontColor = Color.White

    Text(
        text = "${stringResource(R.string.day_feels_like)} ${
            prettyTime(
                applyTimezone(
                    currentWeather.updatedAt,
                    currentWeather.city
                )
            )
        }",
        fontStyle = FontStyle.Italic,
        color = fontColor,
        modifier = Modifier
            .padding(6.dp)
    )
}

// Method to format the date information from the
// repository
fun prettyDate(zonedDateTime: ZonedDateTime): String {
    return zonedDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM '%s'.")).toString()
        .format(zonedDateTime.dayOfMonth.toString())
}

// Method to format the hour information from the
// repository
fun prettyTime(zonedDateTime: ZonedDateTime): String {
    return zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm")).toString()
}

// Method to apply a time zone to a
fun applyTimezone(zonedDateTime: ZonedDateTime, city: City): ZonedDateTime {
    return zonedDateTime.withZoneSameInstant(ZoneId.of(city.timezone))
}
