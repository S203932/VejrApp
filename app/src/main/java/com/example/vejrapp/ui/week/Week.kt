package com.example.vejrapp.ui.week

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
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.WeatherSymbol
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.ui.day.DayViewModel
import com.example.vejrapp.ui.day.Detail
import com.example.vejrapp.ui.day.getCurrentIndex
import com.example.vejrapp.ui.search.SearchBar
import java.time.format.DateTimeFormatter
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
    dayInt: Int
) {
    val weatherImage = weatherIcon.toString()
    val imageRes = weatherImage.mapToYRImageResource()
    var expanded by remember { mutableStateOf(false) }
    val bitmap = getBitmapFromImage(LocalContext.current, imageRes)

    // Crop the transparent/whitespace areas
    val croppedBitmap = cropBitmap(bitmap)

    val fontColor = Color.Black
    Card(
        /*colors = CardDefaults.cardColors(containerColor = Color.Transparent),*/
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            //.height(60.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
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
                if (precipitation != "null%") {
                    Image(
                        painter = rainIcon, //Rain Icon
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(20.dp),
                    )
                    //Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = precipitation, //Rain Forecast
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
                //Spacer(modifier = Modifier.width(50.dp))

            }

            if (expanded) {
                GetDayHours(day = dayInt)
                Spacer(modifier = Modifier.width(10.dp))
                MiniDetailCard(day = dayInt, true)
                Spacer(modifier = Modifier.width(10.dp))

            }
        }
    }
}


@Composable
fun WeekView() {
    val weekViewModel = hiltViewModel<WeekViewModel>()

    val weatherData by weekViewModel.weatherData.collectAsState()
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(weatherData.data.days.size) {
            weatherData.data.days.forEachIndexed { index, item ->
                val indexOfHour12ish = getHourClosestToMidday(item)

                // Need to calculate index of the hour I want to use
                // I can find min and max air temperature in nextSixHours
                // Rain/percipitation can also be found in nextSixHours
                // Condition can also be found in next six hours
                // Instant is gonna be average of the data closest to 12 a clock midday

                // All the data will be taken from the hour closest to midday
                DayCard(
                    avgTemp = item.hours[indexOfHour12ish].data.instant?.details?.airTemperature
                        .toString() + "°",
                    maxTemp = item.hours[indexOfHour12ish].data.nextSixHours?.details?.airTemperatureMax
                        .toString() + "°",
                    minTemp = item.hours[indexOfHour12ish].data.nextSixHours?.details?.airTemperatureMin
                        .toString() + "°",
                    dayOfTheWeek = item.hours[0].time.dayOfWeek.getDisplayName(
                        java.time.format.TextStyle.FULL,
                        Locale.getDefault()
                    ),
                    dayAndMonth = item.hours[0].time.toString(),
                    precipitation = item.hours[indexOfHour12ish].data.nextSixHours?.details?.probabilityOfPrecipitation
                        .toString() + "%",
                    rainIcon = painterResource(id = R.drawable.umbrella),
                    weatherIcon = item.hours[indexOfHour12ish].data.nextSixHours?.summary?.symbolCode
                        ?: WeatherSymbol.heavysnowshowers_polartwilight,
                    dayInt = index
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


@Composable
fun GetDayHours(day: Int) { //This is the same as HourCards except it uses getHours which gets all the hours for the specific day
    val dayViewModel = hiltViewModel<DayViewModel>()
    val weatherData by dayViewModel.weatherData.collectAsState()
    val dayData = getHours(weatherData, day)
    //val hourList = List(24) { index -> (index + startHour) % 24 }
    LazyRow(
        modifier = Modifier
            // This makes the LazyRow take up the full available width
            .padding(6.dp)
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        items(dayData.hours) { hour ->
            CardWithColumnAndRowWeek(hour)
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between cards
        }
    }
}


private fun getHours(weatherData: WeatherData, dayInt: Int): WeatherData.Day {
    val day = WeatherData.Day()
    // adding all hours from the first day as it there can never be more than 24 hours in a day
    for (item in weatherData.data.days[dayInt].hours.subList(
        0,
        weatherData.data.days[dayInt].hours.size
    )) {
        day.hours.add(item)
    }
    return day
}

@OptIn(ExperimentalLayoutApi::class)
//@Preview
@Composable
fun MiniDetailCard(day: Int, isInWeekList: Boolean) {
    val dayViewModel = hiltViewModel<DayViewModel>()
    val weatherData by dayViewModel.weatherData.collectAsState()
    val currentDay = weatherData.data.days[day]

    var indexOfHour = getCurrentIndex(weatherData, day)
    if (isInWeekList) {
        indexOfHour = 0
    }
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

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        /*colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.6f)),*/
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        /*val humidityValue: Float? = 1F
        val windSpeed: Float? = 2F // Replace with actual windSpeed value
        val uvIndex: Float? = 3F // Replace with actual uvIndex value
        val percentageRain: Float? = 4F // Replace with actual percentageRain value
        val rainAmount: Float? = 5F // Replace with actual rainAmount value
        val pressure: Float? = 6F // Replace with actual pressure value
        val probabilityThunder: Float? = null // Replace with actual probabilityThunder value*/

        //if (humidityValue != null) {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Detail(
                painterId = R.drawable.baseline_water_drop_24,
                text = "",  // Set text to null
                value = humidity,
                unit = humidityUnit
            )
            Detail(
                painterId = R.drawable.baseline_air_24,
                text = "",  // Set text to null
                value = windSpeed,
                unit = windSpedUnit
            )
            Detail(
                painterId = R.drawable.outline_wb_sunny_24,
                text = "",  // Set text to null
                value = uvIndex,
                unit = ""  // Set unit to null
            )
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = "",  // Set text to null
                value = percentageRain,
                unit = percentageRainUnit
            )
            Detail(
                painterId = R.drawable.baseline_umbrella_24,
                rotateIcon = true,
                text = "",  // Set text to null
                value = rainAmount,
                unit = " $rainAmountUnit"
            )
            Detail(
                painterId = R.drawable.baseline_compress_24,
                text = "",  // Set text to null
                value = pressure,
                unit = " $pressureUnit"
            )
            Detail(
                painterId = R.drawable.baseline_thunderstorm_24,
                text = "",  // Set text to null
                value = probabilityThunder,
                unit = probabilityThunderUnit
            )
        }
    }
}

@Composable
fun CardWithColumnAndRowWeek(hour: ForecastTimeStep) {
    val weatherImage = hour.data.nextOneHours?.summary?.symbolCode.toString()
    val temperature = hour.data.instant?.details?.airTemperature
    val percentageRain = hour.data.nextOneHours?.details?.probabilityOfPrecipitation
    val rainMin = hour.data.nextOneHours?.details?.precipitationAmountMin?.toInt()
    val rainMax = hour.data.nextOneHours?.details?.precipitationAmountMax?.toInt()
    val imageRes = weatherImage.mapToYRImageResource()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    Card(
        /*colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.6f)),*/
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp

        ),
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