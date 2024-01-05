package com.example.vejrapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        dataStore: DataStore<Preferences>
    ): WeatherRepository {
        return WeatherRepository(locationforecast, dataStore)
    }
}