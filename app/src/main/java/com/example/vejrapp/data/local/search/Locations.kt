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

    var cities = gson.fromJson(
        context.assets.open(citiesAssetPath).bufferedReader().use(BufferedReader::readText),
        Array<City>::class.java
    ).toList()

    var favoriteCities = MutableStateFlow<List<City>>(emptyList())

    // TODO Get favorite city from data store
    val selectedCity = DefaultData.LOCATIONS.CITY

    init {
        scope.launch { getFavoriteCities() }
    }

    fun getFavoriteCities() {
        scope.launch {
            val favoritesCache: List<City>? = getFavoritesFromCache().getOrNull()
            if (favoritesCache != null) {
                favoriteCities.value = favoritesCache
            }

        }
    }

    fun saveFav() {
        scope.launch { saveFavorites() }
    }


    private suspend fun getFavoritesFromCache(): Result<List<City>> {
        val gson = Gson()
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

    private suspend fun saveFavorites() {
        val gson = Gson()
        // val jsonArray = gson.toJsonTree(cities).asJsonArray
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[stringPreferencesKey("CITIES_PREFERENCES_KEY")] =
                    gson.toJson(favoriteCities.value)
            }
        }
    }

//    fun getCityFromLocation(): City {
//
//    }

}

