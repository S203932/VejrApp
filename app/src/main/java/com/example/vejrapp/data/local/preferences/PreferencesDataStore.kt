package com.example.vejrapp.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException

private val Context.dataStore by preferencesDataStore(name = "PreferenceDataStore")

// Used for caching
// not yet implemented
class PreferencesDataStore(context: Context) : IPreferenceDataStoreAPI {

    private val dataSource = context.dataStore

    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
            Flow<T> = dataSource.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }


    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T):
            T = dataSource.data.first()[key] ?: defaultValue

    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataSource.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun <T> clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }


    /*   private companion object {
         val DATA_KEY = booleanPreferencesKey("DATA_KEY")
         const val TAG = "PreferencesStore"
     }


       suspend fun saveFavorite(isFavorite: Boolean) {
             dataStore.edit { preferences ->
                 preferences[DATA_KEY] = isFavorite
             }
         }

         suspend fun removeFavorite(notFavorite: Boolean) {
             dataStore.edit { preferences ->
                 preferences.remove(DATA_KEY)
             }
         }

         val readFavorite: Flow<Boolean> = dataStore.data
             .catch {
                 if (it is IOException) {
                     Log.e(TAG, "Error reading data.", it)
                     emit(emptyPreferences())
                 } else {
                     throw it
                 }
             }
             .map { preferences ->
                 preferences[DATA_KEY] ?: true
             }*/
}