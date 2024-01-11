package com.example.vejrapp.data.repository

import android.util.Log
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.locations.Locations
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.models.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.TimeZone
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val locations: Locations,
    private val locationforecast: Locationforecast,
    private val dataStore: PreferencesDataStore
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    val weatherData =
        MutableStateFlow<WeatherData>(DefaultData.LOCATIONFORECAST.WEATHER_DATA)
    val cities = MutableStateFlow<List<City>>(locations.cities)
    val primaryCity = MutableStateFlow<City?>(null)

    init {
        // Get the selected city and then get the weather data for that city
        scope.launch {
            getSelectedCity()
            getComplete()
        }
        // Get all cities in another coroutine to save time
        scope.launch {
            getCities()
        }
    }


    private suspend fun getSelectedCity() {
        // Get from cache if available, else use dataset
        val cachedSelectedCity = dataStore.getPreferenceSelectedCity()

        if (cachedSelectedCity != null) {
            primaryCity.value = cachedSelectedCity
            Log.d(
                CITIES_DATA_TAG,
                "Using cached selected city ${primaryCity.value?.name} - ${primaryCity.value?.country} with uniqueId ${primaryCity.value?.uniqueId()}"
            )

        } else {
            primaryCity.value = locations.primaryCity
            Log.d(
                CITIES_DATA_TAG,
                "Using default selected city ${primaryCity.value?.name} - ${primaryCity.value?.country} with uniqueId ${primaryCity.value?.uniqueId()}"
            )
        }
    }


    fun updateSelectedCity(newCity: City) {
        primaryCity.value = newCity

        scope.launch {
            dataStore.updatePreferenceSelectedCity(newCity)
        }
    }

    private suspend fun getCities() {
        val cachedCities = dataStore.getPreferenceCities()

        if (cachedCities != null) {
            cities.value = cachedCities
            Log.d(CITIES_DATA_TAG, "Using cached cities")

        } else {
            cities.value = locations.cities
            Log.d(CITIES_DATA_TAG, "Using default cities")
        }
    }

    fun updateCities(newCities: List<City>) {
        cities.value = newCities

        scope.launch {
            dataStore.updatePreferenceCities(newCities)
        }
    }

    fun getComplete() {
        scope.launch {
            var complete: METJSONForecastTimestamped? =
                dataStore.getPreferenceWeatherData(primaryCity.value!!)

            var updateFromApi = true

            // Check if cache has data
            if (complete != null) {
                // Check if cache data is expired by comparing time zone corrected timestamp to current time
                updateFromApi = WeatherUtils.applyTimezone(
                    complete.expires,
                    TimeZone.getDefault()
                ).isBefore(ZonedDateTime.now())
            }

            // Update data from API
            if (updateFromApi) {
                complete = updateComplete()
            } else {
                Log.d(
                    WEATHER_DATA_TAG,
                    "Using cached weather data for ${primaryCity.value?.name} with uniqueId ${primaryCity.value?.uniqueId()} from ${complete!!.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}"
                )
            }

            // Apply data to composable functions
            if (complete != null) {
                weatherData.value = WeatherData(complete, primaryCity.value!!)
            } else {
                Log.d(WEATHER_DATA_TAG, "Unable to establish connection to API server")
            }
        }
    }


    private suspend fun updateComplete(): METJSONForecastTimestamped? {
        val complete = locationforecast.getComplete(
            latitude = primaryCity.value?.latitude!!,
            longitude = primaryCity.value?.longitude!!
        )

        if (complete != null) {
            Log.d(
                WEATHER_DATA_TAG,
                "Data retrieved for ${primaryCity.value?.name} with uniqueId ${primaryCity.value?.uniqueId()} from API from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}."
            )

            // Save data in datastore
            dataStore.updatePreferenceWeatherData(complete, primaryCity.value!!)

            return complete
        }
        return null
    }

    private companion object {
        const val WEATHER_DATA_TAG = "WEATHER_DATA"
        const val CITIES_DATA_TAG = "CITIES_DATA"
    }
}