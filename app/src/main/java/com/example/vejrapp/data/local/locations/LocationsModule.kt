package com.example.vejrapp.data.local.locations

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationsModule {
    @Singleton
    @Provides
    fun provideLocations(
        @ApplicationContext context: Context,
    ): Locations {
        return Locations(context)
    }
}