package com.example.vejrapp.data.repository

import android.util.Log
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.models.CurrentWeather
import com.example.vejrapp.data.repository.models.WeekWeather
import com.example.vejrapp.ui.day.applyTimezone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.TimeZone
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val locationforecast: Locationforecast,
    private val dataStore: PreferencesDataStore
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    var city = DefaultData.LOCATIONS.CITY
    var currentWeather =
        MutableStateFlow<CurrentWeather>(DefaultData.LOCATIONFORECAST.CURRENT_WEATHER)


    // Added weekWeather
    var weekWeather =
        MutableStateFlow<WeekWeather>(DefaultData.LOCATIONFORECAST.WEEK_WEATHER)


    // Get forecast for the default city when starting the app
    init {
        scope.launch {
            getComplete()
        }
    }


    fun getComplete() {
        scope.launch {
            var complete: METJSONForecastTimestamped? =
                dataStore.getPreferenceWeatherData(city).getOrNull()
            var updateFromApi = true

            // Check if cache has data
            if (complete != null) {
                // Check if cache data is expired by comparing time zone corrected timestamp to current time
                updateFromApi = applyTimezone(
                    complete.expires,
                    TimeZone.getDefault()
                ).isBefore(ZonedDateTime.now())
            }

            // Update data from API
            if (updateFromApi) {

                //here
                complete = updateCom()
            } else {
                Log.d(
                    WEATHER_DATA_TAG,
                    "Using cached data for ${city.name} with uniqueId ${city.uniqueId()} from ${complete!!.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}"
                )
            }

            // Apply data to composable functions
            if (complete != null) {
                currentWeather.value = CurrentWeather(complete, city)
                weekWeather.value = WeekWeather(complete)
            } else {
                Log.d(WEATHER_DATA_TAG, "Unable to establish connection to API server")
            }
        }
    }


    private suspend fun updateCom(): METJSONForecastTimestamped? {
        val complete = locationforecast.getComplete(
            latitude = city.latitude,
            longitude = city.longitude
        )
        if (complete != null) {
            Log.d(
                WEATHER_DATA_TAG,
                "Data retrieved for ${city.name} with uniqueId ${city.uniqueId()} from API from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}."
            )

            // Save data in datastore
            dataStore.updatePreferenceWeatherData(complete, city)

            return complete
        }
        return null
    }

    private companion object {
        const val WEATHER_DATA_TAG = "WEATHER_DATA"
    }
}