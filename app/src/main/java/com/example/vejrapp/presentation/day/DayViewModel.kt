package com.example.vejrapp.presentation.day


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.local.search.Locations
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.remote.locationforecast.LocationforecastImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val locationforecast: Locationforecast, locations: Locations
) : ViewModel() {


    private val _complete = MutableStateFlow(LocationforecastImplementation.defaultComplete)
    val complete = _complete.asStateFlow()


    // Top half of dayPage information
    private val currentWeather = complete.value.properties.timeseries[0]
    val currentTemperature = currentWeather.data.instant?.details?.airTemperature
    val currentCondition = currentWeather.data.nextOneHours?.summary?.symbolCode
    val realFeel = currentWeather.data.instant?.details?.dewPointTemperature
    val currentMinTemperature = currentWeather.data.nextOneHours?.details?.airTemperatureMin
    val currentMaxTemperature = currentWeather.data.nextOneHours?.details?.airTemperatureMax
    val currentPercentageRain =
        currentWeather.data.nextOneHours?.details?.probabilityOfPrecipitation
    val currentWindSpeed = currentWeather.data.instant?.details?.windSpeed


    // No weather caution information in API data
    // To make weather caution one must analyze the data oneself and issue warnings accordingly


    // Hourly Data
    val hourlyTemperature = MutableList<Float?>(24) { index ->
        complete.value.properties.timeseries[index].data.instant?.details?.airTemperature
    }
    val hourlyCondition = MutableList<String>(24) { index ->
        complete.value.properties.timeseries[index].data.nextOneHours?.summary?.symbolCode.toString()
    }

    val hourlyPercentageRain = MutableList<Float?>(24) { index ->
        complete.value.properties.timeseries[index].data.nextOneHours?.details?.probabilityOfPrecipitation
    }


    // The middle of DayPage
    // There is no visibility in API Data, can only say how much fog
    val humidity = currentWeather.data.instant?.details?.relativeHumidity
    val uVIndex = currentWeather.data.instant?.details?.ultravioletIndexClearSky
    val pressure = currentWeather.data.instant?.details?.airPressureAtSeaLevel


    private val _city = MutableStateFlow(locations.defaultCity)
    val city = _city.asStateFlow()

    // Get forecast for the default city when starting the app
//    init {
//        updateComplete()
//    }

    fun updateComplete() {
        viewModelScope.launch {
            _complete.value = locationforecast.getComplete(
                latitude = city.value.latitude,
                longitude = city.value.longitude
            )
            Log.d("API call", complete.value.toString())
        }
    }
}