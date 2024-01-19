package com.example.vejrapp.ui.weatherScreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vejrapp.R
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.repository.WeatherUtils
import com.example.vejrapp.data.repository.models.WeatherData
import java.time.format.DateTimeFormatter

@Composable
fun Week(weatherScreenViewModel: WeatherScreenViewModel) {
    val weatherData by weatherScreenViewModel.weatherData.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(weatherData!!.data.days) {
            WeekDay(weatherData!!, it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekDay(weatherData: WeatherData, day: WeatherData.Day) {
    val indexOfHour12ish = getHourClosestToMidday(day)
    val data = day.hours[indexOfHour12ish].data
    val dateTime = day.hours[0].time

    val weatherImage = data.nextSixHours?.summary?.symbolCode.toString()
    val imageRes = weatherImage.mapToYRImageResource()

    var expanded by remember { mutableStateOf(false) }
    val fontColor = Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        onClick = { expanded = !expanded }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Day of week with date and month number
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    // Day of week
                    Text(
                        text = dateTime.format(DateTimeFormatter.ofPattern("E")),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                    // Date and month. Use stringResource to ensure correct month / date placement
                    Text(
                        text = dateTime.format(DateTimeFormatter.ofPattern(stringResource(R.string.week_date_format))),
                        color = fontColor,
                        fontSize = 14.sp
                    )
                }
                //Average Temperature
                Text(
                    text = "${data.instant?.details?.airTemperature}°",
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(Alignment.CenterVertically),
                    color = fontColor,
                    fontSize = 20.sp
                )
                // MIN MAX Temperature
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        text = "${data.nextSixHours?.details?.airTemperatureMax}°",
                        color = fontColor,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${data.nextSixHours?.details?.airTemperatureMin}°",
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
                // Weather Image
                AsyncImage(
                    model = imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(7.dp)
                        .size(50.dp)
                )
                if (data.nextSixHours?.details?.probabilityOfPrecipitation != null) {
                    // Rain Icon
                    Image(
                        painter = painterResource(id = R.drawable.umbrella),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(20.dp),
                    )
                    // Rain Forecast
                    Text(
                        text = "${data.nextSixHours.details.probabilityOfPrecipitation}%",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
            }
        }
        if (expanded) {
            Column {
                HourlyWeather(weatherData, getDayIndex(weatherData, day))
                Spacer(modifier = Modifier.width(10.dp))
                Details(weatherData, getDayIndex(weatherData, day), false)
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

fun getHourClosestToMidday(day: WeatherData.Day): Int {
    var index = 0
    for (hour in day.hours) {
        val indexTime = day.hours[index].time.hour
        val hourTime = hour.time.hour

        if (12 - indexTime >= hourTime - 12) {
            index = day.hours.indexOf(hour)
        }
    }
    return index
}

@Composable
fun HourlyWeather(
    weatherData: WeatherData,
    dayInt: Int,
    overflowHours: Boolean = false,
) {

    val dayData = WeatherUtils.getDayHours(weatherData, dayInt, overflowHours)

    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(8.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(dayData.hours) { hour ->
            WeatherHourCard(hour)
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
        }
    }
}

fun getDayIndex(weatherData: WeatherData, day: WeatherData.Day): Int {
    for ((index, it) in weatherData.data.days.withIndex()) {
        if (it.hours[0].time == day.hours[0].time) {
            return index
        }
    }
    return -1
}
