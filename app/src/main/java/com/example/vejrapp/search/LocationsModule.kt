package com.example.vejrapp.search

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationsModule {
    @Provides
    fun provideLocations(@ApplicationContext context: Context): Locations {
        return Locations(context)
    }
}