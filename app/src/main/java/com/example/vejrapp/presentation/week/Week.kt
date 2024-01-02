package com.example.vejrapp.presentation.week

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vejrapp.R
import com.example.vejrapp.data.cropBitmap
import com.example.vejrapp.data.getBitmapFromImage
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.remote.locationforecast.models.WeatherSymbol
import com.example.vejrapp.presentation.day.prettyDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
                modifier = Modifier.align(Alignment.CenterVertically).size(20.dp),
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
                    text = dayAndMonth.substring(8,10) +"/"+ dayAndMonth.substring(5,7)
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
fun WeekView(
    weekViewModel: IWeekViewModel
) {
    val weekWeather by weekViewModel.weekWeather.collectAsState()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        Row() {
            Text(
                text = prettyDate(weekWeather.expires),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.Black
            )

        }
        Spacer(modifier = Modifier.height(5.dp))
        DayCard(
            avgTemp = weekWeather.day1Temp.toString() + "°",
            maxTemp = weekWeather.day1Max.toString() + "°",
            minTemp = weekWeather.day1Min.toString() + "°",
            dayOfTheWeek = weekWeather.day1Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day1Date.toString(),
            precipitation = weekWeather.day1Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day1Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
            //painterResource(id = R.drawable.clouds)
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add spacing between cards
        DayCard(
            avgTemp = weekWeather.day2Temp.toString() + "°",
            maxTemp = weekWeather.day2Max.toString() + "°",
            minTemp = weekWeather.day2Min.toString() + "°",
            dayOfTheWeek = weekWeather.day2Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day2Date.toString(),
            precipitation = weekWeather.day2Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day2Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))

        DayCard(
            avgTemp = weekWeather.day3Temp.toString() + "°",
            maxTemp = weekWeather.day3Max.toString() + "°",
            minTemp = weekWeather.day3Min.toString() + "°",
            dayOfTheWeek = weekWeather.day3Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day2Date.toString(),
            precipitation = weekWeather.day2Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day3Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))
        DayCard(
            avgTemp = weekWeather.day4Temp.toString() + "°",
            maxTemp = weekWeather.day4Max.toString() + "°",
            minTemp = weekWeather.day4Min.toString() + "°",
            dayOfTheWeek = weekWeather.day4Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day4Date.toString(),
            precipitation = weekWeather.day4Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day4Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))
        DayCard(
            avgTemp = weekWeather.day5Temp.toString() + "°",
            maxTemp = weekWeather.day5Max.toString() + "°",
            minTemp = weekWeather.day5Min.toString() + "°",
            dayOfTheWeek = weekWeather.day5Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day5Date.toString(),
            precipitation = weekWeather.day5Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day5Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))
        DayCard(
            avgTemp = weekWeather.day6Temp.toString() + "°",
            maxTemp = weekWeather.day6Max.toString() + "°",
            minTemp = weekWeather.day6Min.toString() + "°",
            dayOfTheWeek = weekWeather.day6Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day6Date.toString(),
            precipitation = weekWeather.day6Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day6Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))
        DayCard(
            avgTemp = weekWeather.day7Temp.toString() + "°",
            maxTemp = weekWeather.day7Max.toString() + "°",
            minTemp = weekWeather.day7Min.toString() + "°",
            dayOfTheWeek = weekWeather.day7Date.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ),
            dayAndMonth = weekWeather.day7Date.toString(),
            precipitation = weekWeather.day7Rain.toString() + "%",
            rainIcon = painterResource(id = R.drawable.umbrella),
            weatherIcon = weekWeather.day7Condition ?: WeatherSymbol.heavysnowshowers_polartwilight
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun WeekViewPreview() {
    WeekView(weekViewModel = WeekViewModelPreview())
}
