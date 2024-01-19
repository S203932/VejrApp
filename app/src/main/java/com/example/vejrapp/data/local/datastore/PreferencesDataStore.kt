package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.repository.WeatherUtils.gson
import com.example.vejrapp.ui.settings.models.SettingsModel
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    // just my preference of naming including the package name
    name = "com.example.vejrapp.preferences"
)

class PreferencesDataStore(context: Context) {
    private val userDataStorePreferences = context.userDataStore

    //Reading WeatherData from DataStore
    suspend fun getPreferenceWeatherData(city: City): METJSONForecastTimestamped? {
        val value = getFromKey(stringPreferencesKey(city.uniqueId())).getOrNull()
        return gson.fromJson<METJSONForecastTimestamped>(
            value.toString(),
            METJSONForecastTimestamped::class.java
        )
    }

    // Saving WeatherData in DataStore
    suspend fun updatePreferenceWeatherData(complete: METJSONForecastTimestamped, city: City) {
        updateFromKey(stringPreferencesKey(city.uniqueId()), gson.toJson(complete))
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

    suspend fun getPreferencePrimaryCity(): City? {
        val value = getFromKey(PRIMARY_CITY_KEY).getOrNull()

        if (value != null) {
            return gson.fromJson(value.toString(), City::class.java)
        }
        return value
    }

    suspend fun updatePreferencePrimaryCity(city: City?) {
        updateFromKey(PRIMARY_CITY_KEY, gson.toJson(city))
    }

    suspend fun getPreferenceSettings(): SettingsModel? {
        val value = getFromKey(SETTINGS_KEY).getOrNull()

        if (value != null) {
            return gson.fromJson(value.toString(), SettingsModel::class.java)
        }
        return value
    }

    suspend fun updatePreferenceSettings(settingsModel: SettingsModel) {
        updateFromKey(SETTINGS_KEY, gson.toJson(settingsModel))
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

    private suspend fun updateFromKey(key: Preferences.Key<String>, value: String?) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                if (value == null) {
                    preferences.remove(key)
                } else {
                    preferences[key] = value
                }
            }
        }
    }

    private companion object {
        val PRIMARY_CITY_KEY = stringPreferencesKey(
            name = "SELECTED_CITY"
        )
        val CITIES_KEY = stringPreferencesKey(
            name = "CITIES"
        )
        val SETTINGS_KEY = stringPreferencesKey(
            name = "SETTINGS"
        )
    }
}