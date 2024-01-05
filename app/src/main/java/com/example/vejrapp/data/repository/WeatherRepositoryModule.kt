package com.example.vejrapp.data.repository

import android.content.Context
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherRepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepositoryModule(
        @ApplicationContext context: Context,
        locationforecast: Locationforecast
    ): WeatherRepository {
        return WeatherRepository(context, locationforecast)
    }
}