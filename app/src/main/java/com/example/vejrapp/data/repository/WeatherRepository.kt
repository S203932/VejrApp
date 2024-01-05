package com.example.vejrapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.remote.locationforecast.locationforecastGson
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.models.CurrentWeather
import com.example.vejrapp.data.repository.models.WeekWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val locationforecast: Locationforecast,
    private val userDataStorePreferences: DataStore<Preferences>
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
            val cache = getCompleteFromCache()
            var complete: METJSONForecastTimestamped? = null
            // Check if cache has data
            complete = if (cache.isSuccess && cache.getOrNull() != null) {
                println("FROM CACHE")
                locationforecastGson.fromJson<METJSONForecastTimestamped>(
                    cache.getOrNull(),
                    METJSONForecastTimestamped::class.java
                )
            } else {
                // Get data from API
                println("FROM API")
                updateComplete()
            }

            // Apply data to composable functions
            if (complete != null) {
                currentWeather.value = CurrentWeather(complete, city)
                weekWeather.value = WeekWeather(complete)
            }
        }
    }

    private suspend fun getCompleteFromCache(): Result<String?> {
        return Result.runCatching {
            val flow = userDataStorePreferences.data
                .catch { error ->
                    if (error is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw error
                    }
                }
                .map { preferences -> preferences[stringPreferencesKey(city.uniqueId())] }
            val value = flow.firstOrNull()
            value
//            if (value != null) {
//                locationforecastGson.fromJson<METJSONForecastTimestamped>(
//                    value.toString(),
//                    METJSONForecastTimestamped::class.java
//                )
//            }
//            null
        }
    }

    private suspend fun updateComplete(): METJSONForecastTimestamped? {
        val complete = locationforecast.getComplete(
            latitude = city.latitude,
            longitude = city.longitude
        )

        if (complete != null) {
            Log.d(
                "API call",
                "Data from ${complete.metJsonForecast.properties.meta.updatedAt}. Expires ${complete.expires}."
            )

            // Save data in datastore
            Result.runCatching {
                userDataStorePreferences.edit { preferences ->
                    preferences[stringPreferencesKey(city.uniqueId())] =
                        locationforecastGson.toJson(complete)
                }
            }
            return complete
        }
        return null
    }
}