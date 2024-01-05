//package com.example.vejrapp.data.local.datastore
//
//// Used for caching
//// not yet implemented
////class PreferencesDataStore (
//// private val dataStore: DataStore<Preferences>
////) {
////    private companion object {
////        val DATA_KEY = booleanPreferencesKey("DATA_KEY")
////        const val TAG = "PreferencesStore"
////    }
////
////
////    suspend fun saveFavorite(isFavorite: Boolean) {
////        dataStore.edit { preferences ->
////            preferences[DATA_KEY] = isFavorite
////        }
////    }
////
////    suspend fun removeFavorite(notFavorite: Boolean) {
////        dataStore.edit { preferences ->
////            preferences.remove(DATA_KEY)
////        }
////    }
////
////    val readFavorite: Flow<Boolean> = dataStore.data
////        .catch {
////            if (it is IOException) {
////                Log.e(TAG, "Error reading data.", it)
////                emit(emptyPreferences())
////            } else {
////                throw it
////            }
////        }
////        .map { preferences ->
////            preferences[DATA_KEY] ?: true
////        }
////}
//
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.preferencesDataStore
//
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
//    private companion object {
//
//        val KEY_NAME = stringPreferencesKey(
//            name = "name"
//        )
//    }
//}