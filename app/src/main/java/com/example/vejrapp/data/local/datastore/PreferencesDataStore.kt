package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.locationforecastGson
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException


class PreferencesDataStore(context: Context) {
    private val userDataStorePreferences = context.userDataStore

    private val gson = Gson()

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

    // Saving WeatherData in DataStore
    suspend fun updatePreferenceWeatherData(complete: METJSONForecastTimestamped, city: City) {
        Result.runCatching {
            userDataStorePreferences.edit { preferences ->
                preferences[stringPreferencesKey(city.uniqueId())] =
                    locationforecastGson.toJson(complete)
            }
        }
    }


    suspend fun getPreferenceSelectedCity(): City {
        val value = getFromKey(SELECTED_CITY_KEY)
        return gson.fromJson(value.toString(), City::class.java)
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


//class MyUserPreferencesRepository @Inject constructor(
//    private val userDataStorePreferences: DataStore<Preferences>
//) : UserPreferencesRepository {
//
//    override suspend fun setName(
//        name: String
//    ) {
//        Result.runCatching {
//            userDataStorePreferences.edit { preferences ->
//                preferences[KEY_NAME] = name
//            }
//        }
//    }
//
//    override suspend fun getName(): Result<String> {
//        return Result.runCatching {
//            val flow = userDataStorePreferences.data
//                .catch { exception ->
//                    /*
//                     * dataStore.data throws an IOException when an error
//                     * is encountered when reading data
//                     */
//                    if (exception is IOException) {
//                        emit(emptyPreferences())
//                    } else {
//                        throw exception
//                    }
//                }
//                .map { preferences ->
//                    // Get our name value, defaulting to "" if not set
//                    preferences[KEY_NAME]
//                }
//            val value = flow.firstOrNull() ?: "" // we only care about the 1st value
//            value
//        }
//    }
//

//}