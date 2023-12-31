package com.example.vejrapp.data.repository

import android.util.Log
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.repository.models.CurrentWeather
import com.example.vejrapp.data.repository.models.WeekWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface IWeatherRepository {
    val city: City
    val currentWeather: MutableStateFlow<CurrentWeather>


    //Added weekWeather as variable to repository
    val weekWeather: MutableStateFlow<WeekWeather>


    fun updateComplete()
}

class WeatherRepositoryPreview() : IWeatherRepository {
    override val city: City
        get() = TODO("Not yet implemented")

    override val currentWeather: MutableStateFlow<CurrentWeather>
        get() = TODO("Not yet implemented")


    // Added weekWeather to preview
    override val weekWeather: MutableStateFlow<WeekWeather>
        get() = TODO("Not yet implemented")


    override fun updateComplete() {
        TODO("Not yet implemented")
    }
}

class WeatherRepository @Inject constructor(private val locationforecast: Locationforecast) :
    IWeatherRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private var complete = DefaultData.LOCATIONFORECAST.COMPLETE
    override var city = DefaultData.LOCATIONS.CITY
    override var currentWeather =
        MutableStateFlow<CurrentWeather>(DefaultData.LOCATIONFORECAST.CURRENT_WEATHER)


    // Added weekWeather
    override var weekWeather =
        MutableStateFlow<WeekWeather>(DefaultData.LOCATIONFORECAST.WEEK_WEATHER)


    // Get forecast for the default city when starting the app
    init {
        updateComplete()
    }

    override fun updateComplete() {
        scope.launch {
            complete = locationforecast.getComplete(
                latitude = city.latitude,
                longitude = city.longitude
            )
            currentWeather.value = CurrentWeather(complete)
            weekWeather.value = WeekWeather(complete)
            Log.d("API call", complete.toString())
        }
    }
}