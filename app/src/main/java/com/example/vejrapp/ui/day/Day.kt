package com.example.vejrapp.ui.day

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
        Row() {
            Text(
                text = prettyDate(currentWeather.expires),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                color = fontColor
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier
            )
            {
                //Current Temp
                Text(
                    text = currentWeather.currentTemperature.toString() + "°",
                    fontSize = 50.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
                //Realfeel Temp
                Text(
                    text = "Realfeel " + currentWeather.realFeel.toString() + "°", //TODO actual realfeel
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
                //Min-Max Row
                Row {
                    //Max Temp
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_upward_24),
                        contentDescription = "Max",
                        tint = fontColor
                    )
                    Text(
                        text = currentWeather.currentMaxTemperature.toString() + "°",
                        modifier = Modifier
                            .padding(2.dp),
                        color = fontColor
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    //Min Temp
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_downward_24),
                        contentDescription = "Min",
                        tint = fontColor
                    )
                    Text(
                        text = currentWeather.currentMinTemperature.toString() + "°",
                        modifier = Modifier
                            .padding(2.dp),
                        color = fontColor
                    )

                }
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    Text(
                        text = prettyTime(
                            applyTimezone(
                                currentWeather.updatedAt,
                                currentWeather.city
                            )
                        ),
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(2.dp),
                        color = fontColor
                    )
                    Icon(
                        painter = painterResource(R.drawable.baseline_sync_24),
                        contentDescription = "Update data",
                        tint = fontColor
                    )
                }

            }
            Spacer(modifier = Modifier.width(50.dp))
            Column(
                modifier = Modifier
            )
            {
                Image(
                    bitmap = croppedBitmap.asImageBitmap(),
                    contentDescription = "Weather icon",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Row {
                    // Remove rain if no data is available
                    if (currentWeather.currentPercentageRain != null) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_umbrella_24),
                            contentDescription = "Weather icon",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .rotate(180F),
                            tint = fontColor

                        )
                        Text(
                            text = currentWeather.currentPercentageRain.toString() + "%",
                            modifier = Modifier
                                .padding(2.dp),
                            color = fontColor
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Icon(
                        painter = painterResource(R.drawable.baseline_air_24),
                        contentDescription = "Weather icon",
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                        tint = fontColor
                    )
                    Text(
                        text = currentWeather.currentWindSpeed.toString() + " m/s",
                        modifier = Modifier
                            .padding(2.dp),
                        color = fontColor
                    )
                }
            }
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
                text = "Caution:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = fontColor
            )
            Text(
                text = "Rainy weather in coming days",
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
                text = currentWeather.hourlyTemperature[hour].toString() + "°",
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
                        painter = painterResource(id = R.drawable.baseline_umbrella_24), // Use your own image resource
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .rotate(180F)
                    )

                    // Text
                    Text(
                        text = currentWeather.hourlyPercentageRain[hour].toString() + "%",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Third Text
            Text(
                text = String.format("%02d:00", hour),
                //LocalContext.current.getString(weatherType.stringResourceId),
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
@Composable
fun DetailsBox() {
    val dayViewModel = hiltViewModel<DayViewModel>()
    val currentWeather by dayViewModel.currentWeather.collectAsState()

    val fontColor = Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(6.dp)
    ) {
        Text(
            text = "Details",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
            color = fontColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceEvenly

        ) {
            Detail(
                painterId = R.drawable.baseline_water_drop_24,
                text = "Humidity",
                value = currentWeather.humidity,
                unit = "%"
            )

            Detail(
                painterId = R.drawable.outline_wb_sunny_24,
                text = "UV index",
                value = currentWeather.uvIndex
            )
            Detail(
                painterId = R.drawable.baseline_compress_24,
                text = "Pressure",
                value = currentWeather.pressure,
                unit = "hPa"
            )
        }
    }
}

@Composable
fun Detail(painterId: Int, text: String, value: Float?, unit: String = "") {

    if (value == null) {
        return
    }

    val fontColor = Color.Black

    Column(modifier = Modifier.padding(4.dp)) {
        Icon(
            painter = painterResource(painterId),
            contentDescription = text,
            modifier = Modifier.align(Alignment.CenterHorizontally),
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
