package com.example.vejrapp.data.repository

import android.util.Log
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.data.repository.models.WeekWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val locationforecast: Locationforecast) {
    private val scope = CoroutineScope(Dispatchers.IO)

    var city = DefaultData.LOCATIONS.CITY
    var weatherData =
        MutableStateFlow<WeatherData>(DefaultData.LOCATIONFORECAST.WEATHER_DATA)


    // Added weekWeather
    var weekWeather =
        MutableStateFlow<WeekWeather>(DefaultData.LOCATIONFORECAST.WEEK_WEATHER)


    // Get forecast for the default city when starting the app
    init {
        updateComplete()
    }

    fun updateComplete() {
        scope.launch {
            val complete = locationforecast.getComplete(
                latitude = city.latitude,
                longitude = city.longitude
            )

            if (complete != null) {
                Log.d(
                    "API call",
                    "Data from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}."
                )

                weatherData.value = WeatherData(complete, city)
                weekWeather.value = WeekWeather(complete)
            }
        }
    }
}