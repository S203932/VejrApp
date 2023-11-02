package com.example.vejrapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DayCard(
    avgTemp : String,
    maxTemp : String,
    minTemp : String,
    dayOfTheWeek : String,
    dayAndMonth : String,
    precipitation : String,
    rainIcon : Painter,
    weatherIcon : Painter
) {
    val fontColor = Color.Black
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
        ) {
            Spacer(modifier = Modifier.width(2.dp))
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
                Text(text = maxTemp, color = fontColor, fontSize = 13.sp)
                Text(text = minTemp, color = fontColor, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = weatherIcon, //Weather Symbol for the day
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = rainIcon, //Rain Icon
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                text = precipitation, //Rain Forecast
                modifier = Modifier.align(Alignment.CenterVertically),
                color = fontColor,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(150.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = dayOfTheWeek
                    /*   text = "${DateFormat.DAY_OF_WEEK_FIELD}"*/,
                    color = fontColor,
                    fontSize = 10.sp
                )
                Text(
                    text = dayAndMonth
                    /* text = "${DateFormat.DATE_FIELD}"*/,
                    color = fontColor,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun WeekView() {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Monday",
                dayAndMonth = "23/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp)) // Add spacing between cards
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Tuesday",
                dayAndMonth = "24/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Wednesday",
                dayAndMonth = "25/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Thursday",
                dayAndMonth = "26/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Friday",
                dayAndMonth = "27/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Saturday",
                dayAndMonth = "28/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{ // You can change the number of cards as needed
            DayCard(
                avgTemp = "20°",
                maxTemp = "22°",
                minTemp = "16°",
                dayOfTheWeek = "Sunday",
                dayAndMonth = "29/10",
                precipitation = "30%",
                rainIcon = painterResource(id = R.drawable.umbrella),
                weatherIcon = painterResource(id = R.drawable.clouds)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}