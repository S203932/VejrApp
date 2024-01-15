package com.example.vejrapp.data.repository

import android.content.Context
import android.util.Log
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.data.local.locations.Locations
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.LocationforecastImplementation
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.models.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.TimeZone

class WeatherRepository(context: Context) {
    private val locations = Locations(context)
    private val locationforecast = LocationforecastImplementation()
    private val dataStore = PreferencesDataStore(context)


    private val scope = CoroutineScope(Dispatchers.IO)

    val weatherData = MutableStateFlow<WeatherData?>(null)
    val cities = MutableStateFlow<List<City>?>(null)
    val primaryCity = MutableStateFlow<City?>(null)

    init {
        // Get the selected city and then get the weather data for that city
        scope.launch {
            getSelectedCity()
            getWeatherData()
        }
        // Get all cities in another coroutine to save time
        scope.launch {
            getCities()
        }
    }


    private suspend fun getSelectedCity() {
        // Get from cache if available, else use dataset
        val cachedSelectedCity = dataStore.getPreferenceSelectedCity()

        // Clear cache from non-favorite primary city
        if (cachedSelectedCity != null && !cachedSelectedCity.favorite) {
            dataStore.updatePreferenceSelectedCity(null)
            Log.d(
                CITIES_DATA_TAG,
                "Removing cached selected city ${primaryCity.value?.name} - ${primaryCity.value?.country} with uniqueId ${primaryCity.value?.uniqueId()} as it is no longer favorite"
            )
        }

        // Use default city
        else if (cachedSelectedCity == null) {
            primaryCity.value = locations.primaryCity
            Log.d(
                CITIES_DATA_TAG,
                "Using default selected city ${primaryCity.value?.name} - ${primaryCity.value?.country} with uniqueId ${primaryCity.value?.uniqueId()}"
            )
        }

        // Get city from cache
        else {
            primaryCity.value = cachedSelectedCity
            Log.d(
                CITIES_DATA_TAG,
                "Using cached selected city ${primaryCity.value?.name} - ${primaryCity.value?.country} with uniqueId ${primaryCity.value?.uniqueId()}"
            )
        }
    }


    fun updatePrimaryCity(newCity: City) {
        primaryCity.value = newCity.copy()

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

    private fun dataIsExpired(time: ZonedDateTime): Boolean {
        return WeatherUtils.applyTimezone(
            time,
            TimeZone.getDefault()
        ).isBefore(ZonedDateTime.now())
    }

    suspend fun getWeatherData() {

        // Check if stored WeatherData is valid for use
        if (weatherData.value != null) {
            if (weatherData.value?.city == primaryCity.value && !dataIsExpired(weatherData.value!!.expires)) {
                return
            }
        }

        // Check if cache has data
        var complete: METJSONForecastTimestamped? =
            dataStore.getPreferenceWeatherData(primaryCity.value!!)


//        if (complete != null && dataIsExpired(complete.expires)) {
//            // Check if cache data is expired by comparing time zone corrected timestamp to current time
//
//        }


        // Update data from API
        if (complete == null) {
            complete = updateComplete()
        }

        // Apply data to composable functions
        if (complete != null) {
            weatherData.value = WeatherData(complete, primaryCity.value!!)
            Log.d(
                WEATHER_DATA_TAG,
                "Using cached weather data for ${primaryCity.value?.name} with uniqueId ${primaryCity.value?.uniqueId()} from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}"
            )
        } else {
            weatherData.value = null
            Log.d(
                WEATHER_DATA_TAG,
                "Unable to establish connection to API server and no cached data available"
            )
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