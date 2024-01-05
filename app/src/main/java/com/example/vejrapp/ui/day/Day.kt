package com.example.vejrapp.ui.day

import android.graphics.Typeface
import android.widget.TextClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vejrapp.R
import com.example.vejrapp.data.cropBitmap
import com.example.vejrapp.data.getBitmapFromImage
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStepData
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.ui.search.SearchBar
import java.lang.Math.exp
import java.math.RoundingMode
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

// The page displayed at the landing screen/main screen/ today's weather
@Composable
fun Day(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        SearchBar(onNextButtonClicked = { navController.navigate(Route.Settings.name) })
        LazyColumn {
            item { TopWeather() }
            item { CautionBox() }
            item { LazyRowWithCards() }
            item { DetailsBox() }
        }
    }
}

@Composable
fun TopWeather() {
    val dayViewModel = hiltViewModel<DayViewModel>()

    val weatherData by dayViewModel.weatherData.collectAsState()
    val indexOfHour = getCurrentIndex(weatherData)
    val dataCurrentHour = weatherData.data.days[0].hours[indexOfHour].data
    val weatherImage = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val imageRes = weatherImage.mapToYRImageResource()
    val fontColor = Color.White
    val bitmap = getBitmapFromImage(LocalContext.current, imageRes)

    // Crop the transparent/whitespace areas
    val croppedBitmap = cropBitmap(bitmap)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val clockTypeface =
                LocalFontFamilyResolver.current.resolve(
                    fontWeight = FontWeight.Bold,
                ).value as Typeface

            AndroidView(
                factory = { context ->
                    TextClock(context).apply {
                        format12Hour?.let {
                            this.format12Hour = context.getString(R.string.day_text_clock_12)
                        }
                        format24Hour?.let {
                            this.format24Hour = context.getString(R.string.day_text_clock_24)
                        }
                        textSize.let { this.textSize = 16F }

                        setTextColor(context.getColor(R.color.white))
                        timeZone = weatherData.city.timezone
                        typeface = clockTypeface
                    }
                }, update = { view ->
                    view.timeZone = weatherData.city.timezone
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Temperature block
            Column {
                //Current temperature
                Text(
                    text = "${dataCurrentHour.instant?.details?.airTemperature}°",
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
                        calculateMinTemperature(weatherData),
                        calculateMaxTemperature(weatherData)
                    ),
                    fontSize = 20.sp,
                    color = fontColor,
                    modifier = Modifier
                        .padding(2.dp)
                )
                // Feels like temperature
                Text(
                    text = stringResource(R.string.day_feels_like).format(
                        calculateFeelsLike(dataCurrentHour)
                    ),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
            }
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

// In the LazyRow of the hour view
// this is the composable generating
// each hour section within the hour view
@Composable
fun CardWithColumnAndRow(hour: ForecastTimeStep) {
    val weatherImage = hour.data.nextOneHours?.summary?.symbolCode.toString()
    val temperature = hour.data.instant?.details?.airTemperature
    val percentageRain = hour.data.nextOneHours?.details?.probabilityOfPrecipitation
    val imageRes = weatherImage.mapToYRImageResource()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
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
                text = "${temperature}°",
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(4.dp)
                    // Try to center the text more by offsetting the degree symbol
                    .offset(x = 4.dp)
            )
            // Remove rain if no data is available
            if (percentageRain != null) {
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
                        text = "${currentWeather.hourlyPercentageRainMin[hour]?.toInt()}/${currentWeather.hourlyPercentageRainMax[hour]?.toInt()} mm",
                        //text = "${currentWeather.hourlyPercentageRain[hour]}%",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (currentWeather.currentPercentageRain != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    Text(
                        text = "${percentageRain}%",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            // Third Text
            Text(
                // Need to convert this to string or correct hour
                text = hour.time.format(formatter),
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

    val dayViewModel = hiltViewModel<DayViewModel>()
    val weatherData by dayViewModel.weatherData.collectAsState()
    val day = get24Hours(weatherData)
    //val hourList = List(24) { index -> (index + startHour) % 24 }
    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(6.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(day.hours) { hour ->
            CardWithColumnAndRow(hour)
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
    val weatherData by dayViewModel.weatherData.collectAsState()
    val currentDay = weatherData.data.days[0]
    val indexOfHour = getCurrentIndex(weatherData)
    val dataCurrentHour = currentDay.hours[indexOfHour]

    val humidity = dataCurrentHour.data.instant?.details?.relativeHumidity
    val humidityUnit = weatherData.units.relativeHumidity
    val windSpeed = dataCurrentHour.data.instant?.details?.windSpeed
    val windSpedUnit = weatherData.units.windSpeed
    val uvIndex = dataCurrentHour.data.instant?.details?.ultravioletIndexClearSky
    val percentageRain = dataCurrentHour.data.nextOneHours?.details?.probabilityOfPrecipitation
    val percentageRainUnit = weatherData.units.probabilityOfPrecipitation
    val pressure = dataCurrentHour.data.instant?.details?.airPressureAtSeaLevel
    val pressureUnit = weatherData.units.airPressureAtSeaLevel
    val probabilityThunder = dataCurrentHour.data.instant?.details?.probabilityOfThunder
    val probabilityThunderUnit = weatherData.units.probabilityOfThunder

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
                value = humidity,
                unit = humidityUnit
            )
            Detail(
                painterId = R.drawable.baseline_air_24,
                text = stringResource(R.string.day_wind_speed),
                value = windSpeed,
                unit = " " + windSpedUnit
            )
            Detail(
                painterId = R.drawable.outline_wb_sunny_24,
                text = stringResource(R.string.day_uv_index),
                value = uvIndex
            )
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = stringResource(R.string.day_rain),
                value = percentageRain,
                unit = percentageRainUnit
            )
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = stringResource(R.string.day_rain),
                value = currentWeather.currentPercentageRain,
                unit = " " + currentWeather.units.precipitationAmount
            )
            Detail(
                painterId = R.drawable.baseline_compress_24,
                text = stringResource(R.string.day_pressure),
                value = pressure,
                unit = " " + pressureUnit
            )
            Detail(
                painterId = R.drawable.baseline_thunderstorm_24,
                text = stringResource(R.string.day_thunder),
                value = probabilityThunder,
                unit = probabilityThunderUnit
            )

        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.day_data_updated).format(
                    prettyTime(
                        applyTimezone(weatherData.updatedAt, TimeZone.getDefault()),
                        stringResource(R.string.day_pretty_time)
                    )
                ),
                color = fontColor,
                modifier = Modifier.padding(10.dp)
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
            text = "$value$unit",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = fontColor
        )
    }
}

// Method to format the hour information from the
// repository
fun prettyTime(zonedDateTime: ZonedDateTime, stringResource: String): String {
    return zonedDateTime.format(DateTimeFormatter.ofPattern(stringResource))
        .toString()
}

// Method to apply a time zone to a
fun applyTimezone(zonedDateTime: ZonedDateTime, timeZone: TimeZone): ZonedDateTime {

    return zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone.id))
}

private fun applyCityTimeZone(zonedDateTime: ZonedDateTime, city: City): ZonedDateTime {
    return zonedDateTime.withZoneSameInstant(ZoneId.of(city.timezone))
}


// Method to get current hour in 24 hourFormat
private fun getCurrentHour(city: City): Int {
    val currentTime = ZonedDateTime.now()

    return applyCityTimeZone(currentTime, city).hour
}

// Method to calculate the max temperature of the current day
private fun calculateMaxTemperature(weatherData: WeatherData): Float {
    var maxTemp = -1000F
    for (item in weatherData.data.days[0].hours) {
        if (item.data.instant?.details?.airTemperature ?: -3000f > maxTemp) {
            maxTemp = item.data.instant?.details?.airTemperature ?: -1000F
        }
    }
    return maxTemp
}


private fun calculateMinTemperature(weatherData: WeatherData): Float {
    var minTemp = -1000F
    for (item in weatherData.data.days[0].hours) {
        if (item.data.instant?.details?.airTemperature ?: -3000f > minTemp) {
            minTemp = item.data.instant?.details?.airTemperature ?: -1000F
        }
    }
    return minTemp
}


// Method to get the index for the current hour in the current day
private fun getCurrentIndex(weatherData: WeatherData): Int {
    val currentHour = getCurrentHour(weatherData.city)
    val test = weatherData.data.days[0].hours[0].time.hour


    return weatherData.data.days[0].hours.indexOf(weatherData.data.days[0].hours.find {
        it.time.hour == currentHour
    })
}


// Method to return 24 next hours in a day object
private fun get24Hours(weatherData: WeatherData): WeatherData.Day {
    val day = WeatherData.Day()
    // adding all hours from the first day as it there can never be more than 24 hours in a day
    for (item in weatherData.data.days[0].hours.subList(
        getCurrentIndex(weatherData),
        weatherData.data.days[0].hours.size
    )) {
        day.hours.add(item)
    }
    // adding the remaining hours from the tomorrow to reach 24 hours
    for (item in weatherData.data.days[1].hours.subList(0, 24 - day.hours.size)) {
        day.hours.add(item)
    }
    return day
}


private fun calculateFeelsLike(dataCurrentHour: ForecastTimeStepData): Float? {
    // Calculated using Australian apparent temperature (https://en.wikipedia.org/wiki/Wind_chill)
    // TODO check if this should be changed
    val humidity = dataCurrentHour.instant?.details?.relativeHumidity
    val currentTemperature = dataCurrentHour.instant?.details?.airTemperature
    val currentWindSpeed = dataCurrentHour.instant?.details?.windSpeed
    return try {
        val e =
            (humidity!! / 100) * 6.105F * exp((17.27F * currentTemperature!!) / (237.7 + currentTemperature))
        val at = currentTemperature + (0.33F * e) - (0.70F * currentWindSpeed!!) - 4.00F
        // Round to 1 decimal place
        at.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toFloat()

    } catch (error: Exception) {
        null
    }
}



