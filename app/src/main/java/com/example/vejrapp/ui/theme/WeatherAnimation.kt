package com.example.vejrapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vejrapp.data.repository.models.WeatherData
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

@Composable
fun WeatherAnimation(weatherData: WeatherData) {
    val dataCurrentHour = weatherData.data.days[0].hours[0].data
    val weatherState = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val weatherType: PrecipType
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

