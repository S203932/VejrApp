package com.example.vejrapp.data.remote.locationforecast

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationforecastModule {
    @Provides
    fun provideLocationforecast(): Locationforecast {
        return LocationforecastImplementation()
    }
}