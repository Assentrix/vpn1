package com.example.vpnapp.ui.common.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    }

    // Save a value to DataStore
    suspend fun saveIsUserLoggedIn(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = value
        }
    }

    // Retrieve a value from DataStore
    fun getIsUserLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_USER_LOGGED_IN] ?: false
        }
    }
}
