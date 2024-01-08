package com.example.vejrapp.data.local.search

import android.content.Context
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
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
        dataStore: PreferencesDataStore
    ): Locations {
        return Locations(context, dataStore)
    }
}