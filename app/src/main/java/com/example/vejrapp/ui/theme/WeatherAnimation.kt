package com.example.vejrapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vejrapp.ui.screens.getCurrentIndex
import com.example.vejrapp.ui.screens.screenViewModel
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

@Composable
fun WeatherAnimation(screenViewModel: screenViewModel) {
    val weatherData by screenViewModel.weatherData.collectAsState()
    val indexOfHour = getCurrentIndex(weatherData, 0)
    val dataCurrentHour = weatherData.data.days[0].hours[indexOfHour].data
    var weatherState = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    var weatherType: PrecipType
    //    var sleet: Boolean

    if (weatherState.contains("rain")) {
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.RAIN) }
            })
    } else if (weatherState.contains("snow")) {
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.SNOW) }
            })
    }

}

