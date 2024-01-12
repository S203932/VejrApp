package com.example.vejrapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vejrapp.ui.day.DayViewModel
import com.example.vejrapp.ui.day.getCurrentIndex
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

@Composable
fun WeatherAnimation() {
    val dayViewModel = hiltViewModel<DayViewModel>()
    val weatherData by dayViewModel.weatherData.collectAsState()
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

