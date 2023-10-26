package com.example.vejrapp

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun TopWeather() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row() {
            Text(
                text = "Monday October 9th",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier
            )
            {
                //Current Temp
                Text(
                    text = " 18° ",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,

                    )
                //Realfeel Temp
                Text(
                    text = "Realfeel 16°",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(0.dp),
                    textAlign = TextAlign.Center,
                )
                //Min-Max Row
                Row {
                    //Max Temp
                    Image(
                        painter = painterResource(R.drawable.baseline_arrow_upward_24),
                        contentDescription = "Max"
                    )
                    Text(
                        text = "20°",
                        modifier = Modifier
                            .padding(2.dp),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    //Min Temp
                    Image(
                        painter = painterResource(R.drawable.baseline_arrow_downward_24),
                        contentDescription = "Max"
                    )
                    Text(
                        text = "14°",
                        modifier = Modifier
                            .padding(2.dp),
                    )

                }
                Spacer(modifier = Modifier.height(50.dp))
                Row {
                    Text(
                        text = "23/09 , 15:30",
                        modifier = Modifier
                            .padding(2.dp),
                    )
                    Image(
                        painter = painterResource(R.drawable.baseline_sync_24),
                        contentDescription = "Update data"
                    )
                }

            }

            Spacer(modifier = Modifier.width(50.dp))

            Column(
                modifier = Modifier
            )
            {
                Image(
                    painter = painterResource(R.drawable.cloudy),
                    contentDescription = "Weather icon",
                    modifier = Modifier
                        .height(130.dp)
                        .width(135.dp)
                )
                Row {
                    Image(
                        painter = painterResource(R.drawable.baseline_umbrella_24),
                        contentDescription = "Weather icon",
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .rotate(180F)
                    )
                    Text(
                        text = "5%",
                        modifier = Modifier
                            .padding(2.dp),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(R.drawable.baseline_air_24),
                        contentDescription = "Weather icon",
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                    Text(
                        text = "10 m/s",
                        modifier = Modifier
                            .padding(2.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Column {
            CautionBox()
        }

    }

}


@Composable
fun CautionBox() {
    Card(
        colors = CardDefaults.cardColors(  containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Caution:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "Rainy weather in coming days")
        }
    }
}

//Inserting Urban's stuff

@Composable
fun CardWithColumnAndRow(
//weatherType: WeatherType
) {
    Card(
        colors = CardDefaults.cardColors(  containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .width(50.dp) // Set the card's width
            .height(125.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            //Image (you can replace the URL with your image source)
            Image(
                painter = painterResource(id = R.drawable.cloudy), // Use your own image resource
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )

            // Second Text
            Text(
                text = "21°",
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Image (you can replace the URL with your image source)
                Image(
                    painter = painterResource(id = R.drawable.baseline_umbrella_24), // Use your own image resource
                    contentDescription = null,
                    modifier = Modifier
                        .size(10.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .rotate(180F)
                )

                // Text
                Text(
                    text = "100%",
                    fontSize = 8.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            // Third Text
            Text(
                text = "00:00"
                //LocalContext.current.getString(weatherType.stringResourceId)
                ,
                fontSize = 8.sp,
                modifier = Modifier.padding(4.dp)
            )

            // Row with Image and Text

        }
    }
    Spacer(modifier = Modifier.width(4.dp))
}

//@Composable
//fun LazyRowWithCards(weatherTypeList: List<WeatherType>
// ) {
//    LazyRow(
//        modifier = Modifier
//            .fillMaxSize() // This makes the LazyRow take up the full available width
//            .padding(8.dp)
//    ) {
//        items(weatherTypeList) { weatherType ->
//            CardWithColumnAndRow(weatherType = weatherType)
//            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
//        }
//    }
//}
@Preview
@Composable
fun LazyRowWithCards() {
    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(8.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(24) { // You can change the number of cards as needed
            CardWithColumnAndRow()
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
//@Preview
//@Composable
//fun CardWithColumnAndRowPreview() {
//    LazyRowWithCards()
//    //LazyRowWithCards(weatherTypeList = DataSource().loadWeatherType,

//}
@Preview
@Composable
fun DetailsBox() {
    Card(
        colors = CardDefaults.cardColors( containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Details",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceEvenly

        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    painter = painterResource(R.drawable.baseline_water_drop_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Humidity",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "56%",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    painter = painterResource(R.drawable.outline_visibility_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Visibility",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "24100 m",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    painter = painterResource(R.drawable.outline_wb_sunny_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "UV index",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Low (1)",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    painter = painterResource(R.drawable.baseline_compress_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Pressure",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "756,06 mmHg",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
