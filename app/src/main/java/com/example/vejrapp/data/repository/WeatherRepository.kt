package com.example.vejrapp.data.repository

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getString
import com.example.vejrapp.R
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.locations.Locations
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.LocationforecastImplementation
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.WeatherUtils.TAGS.CITIES_DATA_TAG
import com.example.vejrapp.data.repository.WeatherUtils.TAGS.SETTINGS_DATA_TAG
import com.example.vejrapp.data.repository.WeatherUtils.TAGS.WEATHER_DATA_TAG
import com.example.vejrapp.data.repository.models.WeatherData
import com.example.vejrapp.ui.settings.models.SettingModel
import com.example.vejrapp.ui.settings.models.SettingsModel
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
    val primaryCity = MutableStateFlow<City?>(DefaultData.LOCATIONS.CITY)
    val settings = MutableStateFlow(
        SettingsModel(
            temperatureSetting = SettingModel(
                name = getString(context, R.string.settings_temperature_name),
                choiceUnit = getString(
                    context,
                    R.string.settings_temperature_choice_fahrenheit_unit
                ),
                choices = mapOf(
                    Pair(false, getString(context, R.string.settings_temperature_choice_celsius)),
                    Pair(true, getString(context, R.string.settings_temperature_choice_fahrenheit))
                ),

                ), windSpeedSetting = SettingModel(
                name = getString(context, R.string.settings_wind_speed_name),
                choiceUnit = getString(context, R.string.settings_wind_speed_choice_kmh_unit),
                choices = mapOf(
                    Pair(false, getString(context, R.string.settings_wind_speed_choice_ms)),
                    Pair(true, getString(context, R.string.settings_wind_speed_choice_kmh))
                )
            ), pressureSetting = SettingModel(
                name = getString(context, R.string.settings_pressure_name),
                choiceUnit = getString(
                    context,
                    R.string.settings_pressure_choice_atm_unit
                ),
                choices = mapOf(
                    Pair(false, getString(context, R.string.settings_pressure_choice_pa)),
                    Pair(true, getString(context, R.string.settings_pressure_choice_atm))
                )
            )
        )
    )

    init {
        // Get the primary city and then get the weather data for that city
        // Get all available cities
        // Get settings
        scope.launch {
            getSettings()
            getPrimaryCity()
            getWeatherData()
            getCities()
        }
    }


    private suspend fun getPrimaryCity() {
        // Get from cache if available, else use dataset
        val cachedPrimaryCity = dataStore.getPreferencePrimaryCity()

        // Clear cache from non-favorite primary city
        if (cachedPrimaryCity != null && !cachedPrimaryCity.favorite) {
            dataStore.updatePreferencePrimaryCity(null)
            Log.d(
                CITIES_DATA_TAG,
                "Removing cached primary city ${primaryCity.value!!.getVerboseName()} as it is no longer favorite"
            )
        }

        // Use default city
        else if (cachedPrimaryCity == null) {
            primaryCity.value = locations.primaryCity
            Log.d(
                CITIES_DATA_TAG,
                "Using default primary city ${primaryCity.value!!.getVerboseName()}"
            )
        }

        // Get city from cache
        else {
            primaryCity.value = cachedPrimaryCity
            Log.d(
                CITIES_DATA_TAG,
                "Using cached primary city ${primaryCity.value!!.getVerboseName()}"
            )
        }
    }


    fun updatePrimaryCity(newCity: City) {
        primaryCity.value = newCity.copy()

        scope.launch {
            dataStore.updatePreferencePrimaryCity(newCity)
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
        // Check if cache has data
        var complete: METJSONForecastTimestamped? =
            dataStore.getPreferenceWeatherData(primaryCity.value!!)

        var shouldUpdate = false

        // Check if data is cached but expired and should be updated
        if (complete != null) {
            if (dataIsExpired(complete.expires)) {
                shouldUpdate = true
            } else {
                Log.d(
                    WEATHER_DATA_TAG,
                    "Using cached weather data for ${primaryCity.value!!.getVerboseName()} from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}"
                )
            }
        }

        // Check if data should be updated
        if (complete == null || shouldUpdate) {
            // Check if data can be updated
            val tempComplete = updateComplete()

            if (tempComplete != null) {
                // Data has bee updated
                complete = tempComplete
            }
        }


        // Change weatherData according to the results
        if (complete != null) {
            weatherData.value = WeatherData(complete, primaryCity.value!!, settings.value)
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
                "Data retrieved for ${primaryCity.value!!.getVerboseName()} from API from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}."
            )

            // Save data in datastore
            dataStore.updatePreferenceWeatherData(complete, primaryCity.value!!)

            return complete
        }
        return null
    }

    private suspend fun getSettings() {
        val cachedSettings = dataStore.getPreferenceSettings()

        if (cachedSettings != null) {
            settings.value = cachedSettings
            Log.d(SETTINGS_DATA_TAG, "Using cached settings.")

        } else {
            Log.d(SETTINGS_DATA_TAG, "No cache available for settings. Using default settings.")
        }
    }

    suspend fun updateSettings() {
        dataStore.updatePreferenceSettings(settings.value)
        getWeatherData()
    }
}