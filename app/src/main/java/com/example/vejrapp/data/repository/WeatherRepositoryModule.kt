package com.example.vejrapp.data.repository

import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherRepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepositoryModule(
        locationforecast: Locationforecast,
        //  dataStore: DataStore<Preferences>,
        dataStore: PreferencesDataStore
    ): WeatherRepository {
        return WeatherRepository(locationforecast, dataStore)
    }
}