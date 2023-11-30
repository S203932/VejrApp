package com.example.vejrapp.data.remote.locationforecast

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Used for creating the forecast variable
// Singleton pattern is used to only creating one instance of the data
@Module
@InstallIn(SingletonComponent::class)
object LocationforecastModule {
    @Singleton
    @Provides
    fun provideLocationforecast(): Locationforecast {
        return LocationforecastImplementation()
    }
}