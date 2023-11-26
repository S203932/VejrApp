package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//val Context.cacheDatastore: DataStore<Preferences> by preferencesDataStore(name = "current_weather_cache")
const val WEATHER_CACHE = "weather_cache"

@Module
@InstallIn(SingletonComponent::class)
object WeatherCacheDataStoreModule {
    @Singleton
    @Provides
    fun provideWeatherCacheDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
//            migrations = listOf(SharedPreferencesMigration(context, CACHE)),
            produceFile = { context.preferencesDataStoreFile(WEATHER_CACHE) }
        )
    }
}