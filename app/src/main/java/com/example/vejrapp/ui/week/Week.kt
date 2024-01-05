package com.example.vejrapp.ui.week

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.R
import com.example.vejrapp.data.cropBitmap
import com.example.vejrapp.data.getBitmapFromImage
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.remote.locationforecast.models.WeatherSymbol
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.ui.search.SearchBar
import java.util.Locale

@Composable
fun WeekPage(navController: NavHostController = rememberNavController()) {

    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(Route.Settings.name)
            }
        )
        WeekView()
    }
}

@Composable
fun DayCard(
    avgTemp: String,
    maxTemp: String,
    minTemp: String,
    dayOfTheWeek: String,
    dayAndMonth: String,
    precipitation: String,
    rainIcon: Painter,
    weatherIcon: WeatherSymbol
) {

    val weatherImage = weatherIcon.toString()
    val imageRes = weatherImage.mapToYRImageResource()

    val bitmap = getBitmapFromImage(LocalContext.current, imageRes)

    // Crop the transparent/whitespace areas
    val croppedBitmap = cropBitmap(bitmap)

    val fontColor = Color.Black
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Text( //Average Temperature
                text = avgTemp,
                modifier = Modifier
                    .padding(3.dp, 3.dp)
                    .align(Alignment.CenterVertically),
                color = fontColor,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(7.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {//MIN MAX Temperature
                Text(text = maxTemp, color = fontColor, fontSize = 16.sp)
                Text(text = minTemp, color = fontColor, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                bitmap = croppedBitmap.asImageBitmap(),
                //painter = weatherIcon, //Weather Symbol for the day
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(7.dp)
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = rainIcon, //Rain Icon
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(20.dp),
            )
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                text = precipitation, //Rain Forecast
                modifier = Modifier.align(Alignment.CenterVertically),
                color = fontColor,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(50.dp))

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
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


@Composable
fun WeekView() {
    val weekViewModel = hiltViewModel<WeekViewModel>()

    val weatherData by weekViewModel.weatherData.collectAsState()

    Column {
        Row() {
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(Alignment.BottomCenter)
        ) {
            weatherData.data.days.forEach {
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
                        java.time.format.TextStyle.FULL,
                        Locale.getDefault()
                    ),
                    dayAndMonth = it.hours[0].time.toString(),
                    precipitation = it.hours[indexOfHour12ish].data.nextSixHours?.details?.probabilityOfPrecipitation
                        .toString() + "%",
                    rainIcon = painterResource(id = R.drawable.umbrella),
                    weatherIcon = it.hours[indexOfHour12ish].data.nextSixHours?.summary?.symbolCode
                        ?: WeatherSymbol.heavysnowshowers_polartwilight
                )


                Spacer(modifier = Modifier.height(8.dp))

            }
        }

    }
}

private fun getHourClosestToMidday(day: WeatherData.Day): Int {
    var index = 0
    for (hour in day.hours) {
        if (12 - day.hours[index].time.hour > 12 - hour.time.hour) {
            index = day.hours.indexOf(day.hours.find {
                it.time.hour == it.time.hour
            })
        }
    }
    return index
}


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

