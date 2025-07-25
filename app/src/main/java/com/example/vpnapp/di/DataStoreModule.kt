package com.example.vpnapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.vpnapp.ui.common.repositories.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.dataStoreFile("app_preferences.preferences_pb")
        }
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>
    ): DataStoreRepository {
        return DataStoreRepository(dataStore)
    }
}
