package com.example.vejrapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    // just my preference of naming including the package name
    name = "com.example.vejrapp.preferences"
)

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {

    // binds instance of MyUserPreferencesRepository
//    @Binds
//    @Singleton
//    abstract fun bindUserPreferencesRepository(
//        myUserPreferencesRepository: WeatherRepository
//    ): WeatherRepository

    companion object {

        // provides instance of DataStore
        @Provides
        @Singleton
        fun provideUserDataStorePreferences(
            @ApplicationContext applicationContext: Context
        ): PreferencesDataStore {
            return PreferencesDataStore(applicationContext)
        }
    }
}