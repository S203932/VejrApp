package com.example.vejrapp.presentation.day

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.R
import com.example.vejrapp.data.cropBitmap
import com.example.vejrapp.data.getBitmapFromImage
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.presentation.search.ISearchViewModel
import com.example.vejrapp.presentation.search.SearchBar
import com.example.vejrapp.presentation.search.SearchViewModelPreview


@Composable
fun DayPage(
    navController: NavHostController,
    searchViewModel: ISearchViewModel,
    dayViewModel: IDayViewModel
) {
    Column {
        SearchBar(
            onNextButtonClicked = { navController.navigate(Route.Settings.name) },
            navController = navController,
            searchViewModel = searchViewModel
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            item { TopWeather(dayViewModel) }
            item { CautionBox() }
            item { LazyRowWithCards(dayViewModel) }
            item { DetailsBox(dayViewModel) }
//            item {
//                Spacer(modifier = Modifier.height(6.dp))
//                WeekView()
//            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DayPagePreview() {
    DayPage(
        navController = rememberNavController(),
        searchViewModel = SearchViewModelPreview(),
        dayViewModel = DayViewModelPreview()
    )
}

//@Preview
@Composable
fun TopWeather(dayViewModel: IDayViewModel) {
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
                text = "Monday October 9th",
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
                        text = "23/09 , 15:30",
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
                        .width(140.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Row {
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


@Composable
@Preview
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


//Inserting Urban's stuff

@Composable
fun CardWithColumnAndRow(dayViewModel: IDayViewModel, hour: Int) {
    val currentWeather by dayViewModel.currentWeather.collectAsState()
    val weatherImage = currentWeather.hourlyCondition[hour]
    val imageRes = weatherImage.mapToYRImageResource()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .width(100.dp) // Set the card's width
            .height(200.dp),
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
                modifier = Modifier.padding(4.dp)
            )
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
                    text = currentWeather.hourlyTemperature[hour].toString() + "%",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Third Text
            Text(
                text = String.format("%02d:00", hour),
                //LocalContext.current.getString(weatherType.stringResourceId),
                fontSize = 16.sp,
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

@Composable
fun LazyRowWithCards(dayViewModel: IDayViewModel) {
    val hourList = List(24) { index -> index + 1 }
    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(6.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(hourList) { hourList -> // You can change the number of cards as needed
            CardWithColumnAndRow(dayViewModel, hourList)
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Preview
//@Composable
//fun CardWithColumnAndRowPreview() {
//    LazyRowWithCards()
//    //LazyRowWithCards(weatherTypeList = DataSource().loadWeatherType,

//}


@Composable
fun DetailsBox(dayViewModel: IDayViewModel) {
    val fontColor = Color.Black
    val currentWeather by dayViewModel.currentWeather.collectAsState()
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(6.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Details",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp),
                color = fontColor
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            Arrangement.SpaceEvenly

        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.baseline_water_drop_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = fontColor
                )
                Text(
                    text = "Humidity",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
                Text(
                    text = currentWeather.humidity.toString() + "%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.outline_visibility_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = fontColor
                )
                Text(
                    text = "Visibility",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
                Text(
                    text = "n/a",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.outline_wb_sunny_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = fontColor
                )
                Text(
                    text = "UV index",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
                Text(
                    text = currentWeather.uVIndex.toString(),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
            }

            Column(modifier = Modifier.padding(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.baseline_compress_24),
                    contentDescription = "Humidity",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = fontColor
                )
                Text(
                    text = "Pressure",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
                Text(
                    text = currentWeather.pressure.toString() + " hPa",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = fontColor
                )
            }
        }
    }
}

/*
@DrawableRes
fun String.mapToYRImageResource(): Int =
    when (this) {
        "clearsky_day" -> {
            R.drawable.clearsky_day
        }

        "clearsky_night" -> {
            R.drawable.clearsky_night
        }

        "clearsky_polartwilight" -> {
            R.drawable.clearsky_polartwilight
        }

        "fair_day" -> {
            R.drawable.fair_day
        }

        "fair_night" -> {
            R.drawable.fair_night
        }

        "fair_polartwilight" -> {
            R.drawable.fair_polartwilight
        }

        "lightssnowshowersandthunder_day" -> {
            R.drawable.lightrainshowersandthunder_day
        }

        "lightssnowshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssnowshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "lightsnowshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightsnowshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightsnowshowers_polartwilight" -> {
            R.drawable.lightsnowshowers_polartwilight
        }

        "heavyrainandthunder" -> {
            R.drawable.heavyrainandthunder
        }

        "heavysnowandthunder" -> {
            R.drawable.heavysnowandthunder
        }

        "rainandthunder" -> {
            R.drawable.rainandthunder
        }

        "heavysleetshowersandthunder_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowersandthunder_night" -> {
            R.drawable.heavysleetshowersandthunder_night
        }

        "heavysleetshowersandthunder_polartwilight" -> {
            R.drawable.heavysleetshowersandthunder_polartwilight
        }

        "heavysnow" -> {
            R.drawable.heavysnow
        }

        "heavyrainshowers_day" -> {
            R.drawable.heavyrainshowers_day
        }

        "heavyrainshowers_night" -> {
            R.drawable.heavyrainshowers_night
        }

        "heavyrainshowers_polartwilight" -> {
            R.drawable.heavyrainshowers_polartwilight
        }

        "lightsleet" -> {
            R.drawable.lightsleet
        }

        "heavyrain" -> {
            R.drawable.heavyrain
        }

        "lightrainshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightrainshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightrainshowers_polartwilight" -> {
            R.drawable.lightrainshowers_polartwilight
        }

        "heavysleetshowers_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowers_night" -> {
            R.drawable.heavysleetshowers_night
        }

        "heavysleetshowers_polartwilight" -> {
            R.drawable.heavysleetshowers_polartwilight
        }

        "lightsleetshowers_day" -> {
            R.drawable.lightsleetshowers_day
        }

        "lightsleetshowers_night" -> {
            R.drawable.lightsleetshowers_night
        }

        "lightsleetshowers_polartwilight" -> {
            R.drawable.lightsleetshowers_polartwilight
        }

        "snow" -> {
            R.drawable.snow
        }

        "heavyrainshowersandthunder_day" -> {
            R.drawable.heavyrainshowersandthunder_day
        }

        "heavyrainshowersandthunder_night" -> {
            R.drawable.heavyrainshowersandthunder_night
        }

        "heavyrainshowersandthunder_polartwilight" -> {
            R.drawable.heavyrainshowersandthunder_polartwilight
        }

        "snowshowers_day" -> {
            R.drawable.snowshowers_day
        }

        "snowshowers_night" -> {
            R.drawable.snowshowers_night
        }

        "snowshowers_polartwilight" -> {
            R.drawable.snowshowers_polartwilight
        }

        "fog" -> {
            R.drawable.fog
        }

        "snowshowersandthunder_day" -> {
            R.drawable.snowshowersandthunder_day
        }

        "snowshowersandthunder_night" -> {
            R.drawable.snowshowersandthunder_night
        }

        "snowshowersandthunder_polartwilight" -> {
            R.drawable.snowshowersandthunder_polartwilight
        }

        "lightsnowandthunder" -> {
            R.drawable.lightsnowandthunder
        }

        "heavysleetandthunder" -> {
            R.drawable.heavysleetandthunder
        }

        "lightrain" -> {
            R.drawable.lightrain
        }

        "rainshowersandthunder_day" -> {
            R.drawable.rainshowersandthunder_day
        }

        "rainshowersandthunder_night" -> {
            R.drawable.rainshowersandthunder_night
        }

        "rainshowersandthunder_polartwilight" -> {
            R.drawable.rainshowersandthunder_polartwilight
        }

        "rain" -> {
            R.drawable.rain
        }

        "lightsnow" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_day" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_night" -> {
            R.drawable.lightrainshowersandthunder_night
        }

        "lightrainshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "heavysleet" -> {
            R.drawable.heavysleet
        }

        "sleetandthunder" -> {
            R.drawable.sleetandthunder
        }

        "lightrainandthunder" -> {
            R.drawable.lightrainandthunder
        }

        "sleet" -> {
            R.drawable.sleet
        }

        "lightssleetshowersandthunder_day" -> {
            R.drawable.lightssleetshowersandthunder_day
        }

        "lightssleetshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssleetshowersandthunder_polartwilight" -> {
            R.drawable.lightssleetshowersandthunder_polartwilight
        }

        "lightsleetandthunder" -> {
            R.drawable.lightsleetandthunder
        }

        "partlycloudy_day" -> {
            R.drawable.partlycloudy_day
        }

        "partlycloudy_night" -> {
            R.drawable.partlycloudy_night
        }

        "partlycloudy_polartwilight" -> {
            R.drawable.partlycloudy_polartwilight
        }

        "sleetshowersandthunder_day" -> {
            R.drawable.sleetshowersandthunder_day
        }

        "sleetshowersandthunder_night" -> {
            R.drawable.sleetshowersandthunder_night
        }

        "sleetshowersandthunder_polartwilight" -> {
            R.drawable.sleetshowersandthunder_polartwilight
        }

        "rainshowers_day" -> {
            R.drawable.rainshowers_day
        }

        "rainshowers_night" -> {
            R.drawable.rainshowers_night
        }

        "rainshowers_polartwilight" -> {
            R.drawable.rainshowers_polartwilight
        }

        "snowandthunder" -> {
            R.drawable.snowandthunder
        }

        "sleetshowers_day" -> {
            R.drawable.sleetshowers_day
        }

        "sleetshowers_night" -> {
            R.drawable.sleetshowers_night
        }

        "sleetshowers_polartwilight" -> {
            R.drawable.sleetshowers_polartwilight
        }

        "cloudy" -> {
            R.drawable.cloudy
        }

        "heavysnowshowersandthunder_day" -> {
            R.drawable.heavysnowshowersandthunder_day
        }

        "heavysnowshowersandthunder_night" -> {
            R.drawable.heavysnowshowersandthunder_night
        }

        "heavysnowshowersandthunder_polartwilight" -> {
            R.drawable.heavysnowshowersandthunder_polartwilight
        }

        "heavysnowshowers_day" -> {
            R.drawable.heavysnowshowers_day
        }

        "heavysnowshowers_night" -> {
            R.drawable.heavysnowshowers_night
        }

        "heavysnowshowers_polartwilight" -> {
            R.drawable.heavysnowshowers_polartwilight
        }
        else ->{
            R.drawable.cloudy
        }
    }
*/