package com.example.vejrapp.data.local.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.search.models.City
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import javax.inject.Inject

class Locations @Inject constructor(
    private val context: Context,
    private val userDataStorePreferences: DataStore<Preferences>
) {

    private val citiesAssetPath = "filtered_dataset_100000_tz.json"
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _cities = MutableStateFlow<List<City>>(listOf())
    val cities = _cities.asStateFlow()

    var favoriteCities = MutableStateFlow<List<City>>(emptyList())

    // TODO Get favorite city from data store
    val selectedCity = DefaultData.LOCATIONS.CITY

    init {
        getCities()
    }

    private fun getCities() {
        scope.launch {
            // Get from cache if not empty, else load from dataset
            _cities.value = getCitiesFromCache().getOrNull()
                ?: gson.fromJson(
                    context.assets.open(citiesAssetPath).bufferedReader()
                        .use(BufferedReader::readText),
                    Array<City>::class.java
                ).toList()
        }
    }


    private suspend fun getCitiesFromCache(): Result<List<City>> {
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
            val typeToken = object : TypeToken<List<City>>() {}.type
            gson.fromJson<List<City>>(
                value.toString(),
                typeToken
            )
        }
    }

    fun saveCities(newCities: List<City>) {
        scope.launch {
            Result.runCatching {
                userDataStorePreferences.edit { preferences ->
                    preferences[stringPreferencesKey("CITIES_PREFERENCES_KEY")] =
                        gson.toJson(newCities)
                }
            }
        }
    }

    fun saveSelectedCity(newCity: City) {
        
    }
}

