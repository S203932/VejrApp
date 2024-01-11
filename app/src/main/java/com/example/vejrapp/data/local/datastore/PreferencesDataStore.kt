package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.locationforecastGson
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException


class PreferencesDataStore(context: Context) {
    private val userDataStorePreferences = context.userDataStore

    private val gson = Gson()

    //Reading WeatherData from DataStore
    suspend fun getPreferenceWeatherData(city: City): METJSONForecastTimestamped? {
        val value = getFromKey(stringPreferencesKey(city.uniqueId())).getOrNull()
        return locationforecastGson.fromJson<METJSONForecastTimestamped>(
            value.toString(),
            METJSONForecastTimestamped::class.java
        )
    }

    // Saving WeatherData in DataStore
    suspend fun updatePreferenceWeatherData(complete: METJSONForecastTimestamped, city: City) {
        updateFromKey(stringPreferencesKey(city.uniqueId()), locationforecastGson.toJson(complete))
    }

    // Saving Cities in DataStore
    suspend fun getPreferenceCities(): List<City>? {
        val value = getFromKey(CITIES_KEY).getOrNull()

        val typeToken = object : TypeToken<List<City>>() {}.type

        if (value != null) {
            return gson.fromJson<List<City>>(
                value.toString(),
                typeToken
            )
        }
        return value
    }

    // Saving Cities in DataStore
    suspend fun updatePreferenceCities(newCities: List<City>) {
        updateFromKey(CITIES_KEY, gson.toJson(newCities))
    }

    suspend fun getPreferenceSelectedCity(): City? {
        val value = getFromKey(SELECTED_CITY_KEY).getOrNull()

        if (value != null) {
            return gson.fromJson(value.toString(), City::class.java)
        }
        return value
    }

    suspend fun updatePreferenceSelectedCity(city: City) {
        updateFromKey(SELECTED_CITY_KEY, gson.toJson(city))
    }

    private suspend fun getFromKey(key: Preferences.Key<String>): Result<String?> {
        return Result.runCatching {
            val flow = userDataStorePreferences.data
                .catch { error ->
                    if (error is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw error
                    }
                }
                .map { preferences -> preferences[key] }
            flow.firstOrNull()
        }
    }

    private suspend fun updateFromKey(key: Preferences.Key<String>, value: String) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[key] = value
            }
        }
    }

    private companion object {
        val SELECTED_CITY_KEY = stringPreferencesKey(
            name = "SELECTED_CITY"
        )
        val CITIES_KEY = stringPreferencesKey(
            name = "CITIES"
        )
    }
}