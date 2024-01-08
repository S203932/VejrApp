package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vejrapp.data.local.search.models.City
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


    //Reading WeatherData from DataStore
    suspend fun getPreferenceWeatherData(city: City): Result<METJSONForecastTimestamped?> {
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
            locationforecastGson.fromJson<METJSONForecastTimestamped>(
                value.toString(),
                METJSONForecastTimestamped::class.java
            )

        }
    }

    // Saving Cities in DataStore
    suspend fun getCitiesFromCache(): Result<List<City>> {
        return Result.runCatching {
            val flow = userDataStorePreferences.data
                .catch { error ->
                    if (error is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw error
                    }
                }
                .map { preferences -> preferences[stringPreferencesKey("CITIES_PREFERENCES_KEY")] }
            val value = flow.firstOrNull()
            val gson = Gson()
            val typeToken = object : TypeToken<List<City>>() {}.type
            gson.fromJson<List<City>>(
                value.toString(),
                typeToken
            )
        }
    }

    // Saving WeatherData in DataStore
    suspend fun updatePreferenceWeatherData(complete: METJSONForecastTimestamped, city: City) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[stringPreferencesKey(city.uniqueId())] =
                    locationforecastGson.toJson(complete)
            }
        }
    }

    // Saving Cities in DataStore
    suspend fun saveCities(newCities: List<City>) {
        Result.runCatching {
            val gson = Gson()
            userDataStorePreferences.edit { preferences ->
                preferences[stringPreferencesKey("CITIES_PREFERENCES_KEY")] =
                    gson.toJson(newCities)
            }
        }

    }
}

