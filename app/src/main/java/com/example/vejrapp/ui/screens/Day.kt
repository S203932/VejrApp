package com.example.vejrapp.ui.screens

import android.graphics.Typeface
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
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
import coil.compose.AsyncImage
import com.example.vejrapp.R
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStepData
import com.example.vejrapp.data.remote.locationforecast.models.ForecastUnits
import com.example.vejrapp.data.repository.WeatherUtils
import com.example.vejrapp.data.repository.WeatherUtils.applyTimezone
import com.example.vejrapp.data.repository.WeatherUtils.calculateMaxTemperature
import com.example.vejrapp.data.repository.WeatherUtils.calculateMinTemperature
import com.example.vejrapp.data.repository.WeatherUtils.celsiusToFahrenheit
import com.example.vejrapp.data.repository.WeatherUtils.fahrenheitToCelsius
import com.example.vejrapp.data.repository.WeatherUtils.roundFloat
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.ui.theme.WeatherAnimation
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.exp

// The page displayed at the landing screen/main screen/ today's weather
@Composable
fun Day(
    screenViewModel: ScreenViewModel,
    localDateTime: LocalDateTime,
) {
    val weatherData by screenViewModel.weatherData.collectAsState()

    var dayIndex = 0
    if (localDateTime.truncatedTo(ChronoUnit.DAYS) != LocalDateTime.now()
            .truncatedTo(ChronoUnit.DAYS)
    ) {
        dayIndex = abs(localDateTime.compareTo(LocalDateTime.now()))
    }

    if (weatherData != null) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            WeatherAnimation(weatherData!!)
            LazyColumn {
                item { TopWeather(weatherData!!, dayIndex) }
                item { Cautions(weatherData!!, dayIndex) }
                item { HourlyWeather(weatherData!!, dayIndex, true) }
                item { Details(weatherData!!, dayIndex, true) }
            }
        }
    }
}

@Composable
fun TopWeather(weatherData: WeatherData, day: Int) {
    val indexOfHour = getCurrentIndex(weatherData, day)
    val dataCurrentHour = weatherData.data.days[day].hours[indexOfHour].data
    val weatherImage = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val imageRes = weatherImage.mapToYRImageResource()
    val fontColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (day == 0) {
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
            } else {
                val is24HourFormat =
                    android.text.format.DateFormat.is24HourFormat(LocalContext.current)
                val stringResId =
                    if (is24HourFormat) R.string.day_text_clock_24 else R.string.day_text_clock_12
                Text(
                    text = weatherData.data.days[day].hours[0].time.format(
                        DateTimeFormatter.ofPattern(
                            stringResource(id = stringResId) //Shows tomorrows date from 12 am according to Locale (12 or 24 hr format)
                        )
                    ).toString(),
                    fontSize = 16.sp,
                    color = fontColor,
                    fontWeight = FontWeight.Bold
                )
            }

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
                        calculateMinTemperature(weatherData, day),
                        calculateMaxTemperature(weatherData, day)
                    ),
                    fontSize = 20.sp,
                    color = fontColor,
                    modifier = Modifier
                        .padding(2.dp)
                )
                // Feels like temperature
                Text(
                    text = stringResource(R.string.day_feels_like).format(
                        calculateFeelsLike(dataCurrentHour, weatherData.units)
                    ),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                    color = fontColor
                )
            }
            AsyncImage(
                model = imageRes,
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
fun Cautions(weatherData: WeatherData, day: Int) {
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
fun WeatherHourCard(hour: ForecastTimeStep) {
    val weatherImage = hour.data.nextOneHours?.summary?.symbolCode.toString()
    val temperature = hour.data.instant?.details?.airTemperature
    val percentageRain = hour.data.nextOneHours?.details?.probabilityOfPrecipitation
    val rainMin = hour.data.nextOneHours?.details?.precipitationAmountMin?.toInt()
    val rainMax = hour.data.nextOneHours?.details?.precipitationAmountMax?.toInt()
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
            AsyncImage(
                model = imageRes, // Use your own image resource
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
                        text = "${rainMin}/${rainMax} mm",
                        //text = "${currentWeather.hourlyPercentageRain[hour]}%",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (percentageRain != null) {
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


// The bottom section of the main screen displaying
// the additional parameters of the day
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Details(weatherData: WeatherData, day: Int, notInWeekList: Boolean) {
    val currentDay = weatherData.data.days[day]

    // Get the desired index for the hour
    val indexOfHour = if (!notInWeekList) {
        0
    } else {
        getCurrentIndex(weatherData, day)
    }

    // Get the data for the desired hour
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
    val rainAmount = dataCurrentHour.data.instant?.details?.precipitationAmount
    val rainAmountUnit = weatherData.units.precipitationAmount

    val fontColor = Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        if (notInWeekList) {
            Text(
                text = stringResource(R.string.day_details),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                color = fontColor
            )
        }
        FlowRow(
            maxItemsInEachRow = if (notInWeekList) {
                4
            } else {
                Int.MAX_VALUE
            },
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
                unit = humidityUnit,
                notInWeekList = notInWeekList
            )
            Detail(
                painterId = R.drawable.baseline_air_24,
                text = stringResource(R.string.day_wind_speed),
                value = windSpeed,
                unit = " $windSpedUnit",
                notInWeekList = notInWeekList
            )
            Detail(
                painterId = R.drawable.outline_wb_sunny_24,
                text = stringResource(R.string.day_uv_index),
                value = uvIndex,
                notInWeekList = notInWeekList
            )
            // Rain is already shown in day overview
            if (notInWeekList) {
                Detail(
                    painterId = R.drawable.baseline_umbrella_24,
                    rotateIcon = true,
                    text = stringResource(R.string.day_rain),
                    value = percentageRain,
                    unit = percentageRainUnit,
                    notInWeekList = notInWeekList
                )
            }
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = stringResource(R.string.day_rain),
                value = rainAmount,
                unit = " $rainAmountUnit",
                notInWeekList = notInWeekList
            )
            Detail(
                painterId = R.drawable.baseline_compress_24,
                text = stringResource(R.string.day_pressure),
                value = pressure,
                unit = " $pressureUnit",
                notInWeekList = notInWeekList
            )
            Detail(
                painterId = R.drawable.baseline_thunderstorm_24,
                text = stringResource(R.string.day_thunder),
                value = probabilityThunder,
                unit = probabilityThunderUnit,
                notInWeekList = notInWeekList
            )

        }
        if (notInWeekList) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    // Ensure that the data is formatted correctly and uses the local timezone
                    text = stringResource(R.string.day_data_updated).format(
                        prettyTime(
                            applyTimezone(weatherData.updatedAt, TimeZone.getDefault()),
                            if (android.text.format.DateFormat.is24HourFormat(LocalContext.current)) {
                                stringResource(R.string.day_pretty_time_24)
                            } else {
                                stringResource(R.string.day_pretty_time_12)
                            }
                        )
                    ),
                    color = fontColor,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun Detail(
    painterId: Int,
    rotateIcon: Boolean = false,
    text: String,
    value: Float?,
    unit: String? = "",
    notInWeekList: Boolean = true
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
        if (notInWeekList) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = fontColor
            )
        }
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

// Method to get the index for the current hour in the current day
fun getCurrentIndex(weatherData: WeatherData, dayInt: Int): Int {
    val currentHour = if (dayInt > 0) {
        //Set currentHour to 0 so that the day can show from 00:00 for tomorrow page
        0
    } else {
        // Set currentHour to the relevant current time at the given time zone
        applyTimezone(ZonedDateTime.now(), TimeZone.getTimeZone(weatherData.city.timezone)).hour
    }
    val hours = weatherData.data.days[dayInt].hours

    val currentIndex = hours.indexOf(hours.find { it.time.hour == currentHour })

    return if (currentIndex == -1) {
        Log.d(
            WeatherUtils.TAGS.WEATHER_DATA_TAG,
            "Invalid index $currentHour. Using 0 instead for ${weatherData.city.getVerboseName()}"
        )
        0
    } else {
        currentIndex
    }
}


private fun calculateFeelsLike(
    dataCurrentHour: ForecastTimeStepData,
    units: ForecastUnits
): Float? {
    // Calculated using Australian apparent temperature (https://en.wikipedia.org/wiki/Wind_chill)
    val humidity = dataCurrentHour.instant?.details?.relativeHumidity

    // Check if a F -> C conversion should be applied
    val currentTemperature = if (units.airTemperature == "F") {
        fahrenheitToCelsius(dataCurrentHour.instant?.details?.airTemperature!!)
    } else {
        dataCurrentHour.instant?.details?.airTemperature

    }
    // Check if a km/h -> m/s conversion should be made
    val currentWindSpeed = if (units.windSpeed == "m/s") {
        dataCurrentHour.instant?.details?.windSpeed
    } else {
        dataCurrentHour.instant?.details?.windSpeed!! / 3.6F
    }

    return try {
        val e =
            (humidity!! / 100) * 6.105F * exp((17.27F * currentTemperature!!) / (237.7F + currentTemperature))
        val at = currentTemperature + (0.33F * e) - (0.70F * currentWindSpeed!!) - 4.00F

        // Round to 1 decimal place and convert back to Fahrenheit if needed
        roundFloat(
            if (units.airTemperature == "F") {
                celsiusToFahrenheit(at)
            } else {
                at
            }
        )

    } catch (error: Exception) {
        null
    }
}
