package com.example.vpnapp.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vpnapp.R
import com.example.vpnapp.ui.common.repositories.DataStoreRepository
import com.example.vpnapp.ui.settings.model.SettingsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * ViewModel for managing the data and business logic of the Settings screen.
 *
 * This ViewModel is responsible for providing a list of settings options
 * to the SettingsFragment and handles any necessary business logic.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    /**
     * Checks whether the user is logged in.
     *
     * @return Boolean indicating the user's login status.
     */
    private suspend fun isUserLoggedIn(): Boolean {
        return dataStoreRepository.getIsUserLoggedIn().first()
    }

    /**
     * Provides a list of settings options displayed in the settings screen.
     *
     * Each item contains a title and a corresponding navigation destination ID.
     */
    suspend fun getSettingsOptions(): List<SettingsInfo> {
        return if (isUserLoggedIn()) {
            getLoggedInSettings()
        } else {
            getGuestSettings()
        }
    }

    /**
     * Provides the settings options for logged-in users.
     *
     * @return List of [SettingsInfo] for logged-in users.
     */
    private fun getLoggedInSettings(): List<SettingsInfo> {
        return listOf(
            SettingsInfo(
                title = "VPN Settings", // Replace with localized string if necessary
                destinationId = R.id.vpnSettingsFragment
            ),
            SettingsInfo(
                title = "API Access",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            ),
            SettingsInfo(
                title = "App Version",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            ),
            SettingsInfo(
                title = "Report a Problem",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            ),
            SettingsInfo(
                title = "FAQs & Guides",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            )
        )
    }

    /**
     * Provides the settings options for guest users.
     *
     * @return List of [SettingsInfo] for guest users.
     */
    private fun getGuestSettings(): List<SettingsInfo> {
        return listOf(
            SettingsInfo(
                title = "App Info", // Replace with localized string if necessary
                destinationId = R.id.vpnSettingsFragment
            ),
            SettingsInfo(
                title = "Report a problem",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            ),
            SettingsInfo(
                title = "Privacy policy",
                destinationId = R.id.vpnSettingsFragment // Example: Replace with actual destination
            ),
        )
    }
}
