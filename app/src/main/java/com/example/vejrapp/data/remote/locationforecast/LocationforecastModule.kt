package com.example.vejrapp.data.remote.locationforecast

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationforecastModule {
    @Singleton
    @Provides
    fun provideLocationforecast(): Locationforecast {
        return LocationforecastImplementation()
    }
}