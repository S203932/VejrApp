package com.example.vejrapp.data
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
// Used for caching
// not yet implemented
class PreferencesDataStore (
 private val dataStore: DataStore<Preferences>
) {
    private companion object {
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
        }
}