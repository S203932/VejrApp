package com.example.vejrapp.ui.weatherScreens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.vejrapp.R
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.WeatherSymbol
import com.example.vejrapp.data.repository.WeatherUtils
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.ui.screens.Detail
import com.example.vejrapp.ui.screens.getCurrentIndex
import com.example.vejrapp.ui.screens.screenViewModel
import com.example.vejrapp.ui.search.SearchBar
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekPage(screenViewModel: ScreenViewModel) {
    val weatherData by screenViewModel.weatherData.collectAsState()

    WeekView(weatherData!!)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCard(
    avgTemp: String,
    maxTemp: String,
    minTemp: String,
    dayOfTheWeek: String,
    dayAndMonth: String,
    precipitation: String,
    rainIcon: Painter,
    weatherIcon: WeatherSymbol,
    dayInt: Int,
    weatherData: WeatherData
) {
    val weatherImage = weatherIcon.toString()
    val imageRes = weatherImage.mapToYRImageResource()
    var expanded by remember { mutableStateOf(false) }
    val fontColor = Color.Black
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            //.height(60.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        onClick = { expanded = !expanded }
    ) {
        Column(/*modifier = Modifier.background(Color.White.copy(alpha = 0.6f))*/) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                //Spacer(modifier = Modifier.width(50.dp))
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        text = dayOfTheWeek.take(3)
                        /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                        color = fontColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = dayAndMonth.substring(8, 10) + "/" + dayAndMonth.substring(5, 7)
                        /* text = "${DateFormat.DATE_FIELD}"*/,
                        color = fontColor,
                        fontSize = 12.sp
                    )
                }
                //Spacer(modifier = Modifier.width(10.dp))
                Text( //Average Temperature
                    text = avgTemp,
                    modifier = Modifier
                        .padding(3.dp, 3.dp)
                        .align(Alignment.CenterVertically),
                    color = fontColor,
                    fontSize = 20.sp
                )
                //Spacer(modifier = Modifier.width(7.dp))
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {//MIN MAX Temperature
                    Text(text = maxTemp, color = fontColor, fontSize = 16.sp)
                    Text(text = minTemp, color = fontColor, fontSize = 16.sp)
                }
                //Spacer(modifier = Modifier.width(7.dp))
                AsyncImage(
                    model = imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(7.dp)
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                if (precipitation != "null%") {
                    Image(
                        painter = rainIcon, //Rain Icon
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(20.dp),
                    )
                    Text(
                        text = precipitation, //Rain Forecast
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
            }
        }
        if (expanded) {
            Column {
                HourlyWeather(weatherData, dayInt)
                Spacer(modifier = Modifier.width(10.dp))
                Details(weatherData, dayInt, false)
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun WeekView(weatherData: WeatherData) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(weatherData.data.days) {
            val indexOfHour12ish = getHourClosestToMidday(it)
            // Need to calculate index of the hour I want to use
            // I can find min and max air temperature in nextSixHours
            // Rain/percipitation can also be found in nextSixHours
            // Condition can also be found in next six hours
            // Instant is gonna be average of the data closest to 12 a clock midday

            // All the data will be taken from the hour closest to midday
            DayCard(
                avgTemp = it.hours[indexOfHour12ish].data.instant?.details?.airTemperature
                    .toString() + "°",
                maxTemp = it.hours[indexOfHour12ish].data.nextSixHours?.details?.airTemperatureMax
                    .toString() + "°",
                minTemp = it.hours[indexOfHour12ish].data.nextSixHours?.details?.airTemperatureMin
                    .toString() + "°",
                dayOfTheWeek = it.hours[0].time.dayOfWeek.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                ),
                dayAndMonth = it.hours[0].time.toString(),
                precipitation = it.hours[indexOfHour12ish].data.nextSixHours?.details?.probabilityOfPrecipitation
                    .toString() + "%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = it.hours[indexOfHour12ish].data.nextSixHours?.summary?.symbolCode
                    ?: WeatherSymbol.heavysnowshowers_polartwilight,
                dayInt = getDayIndex(weatherData, it),
                weatherData
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun getHourClosestToMidday(day: WeatherData.Day): Int {
    var index = 0
    for (hour in day.hours) {
        var indexTime = day.hours[index].time.hour
        var hourTime = hour.time.hour

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
