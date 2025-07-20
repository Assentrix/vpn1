package com.example.vpnapp.ui.tooManyDevice.repository

import com.example.vpnapp.network.ApiService
import com.example.vpnapp.ui.tooManyDevice.model.LoggedDeviceInfo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TooManyDeviceRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getLoggedDevices(): Result<MutableList<LoggedDeviceInfo>> {
        // TODO: Implement API call to fetch logged devices
        delay(5000)
        return Result.success(
            mutableListOf(
                LoggedDeviceInfo(deviceName = "Device 1", deviceCreatedDate = "Created: 28/9/2024"),
                LoggedDeviceInfo(deviceName = "Device 2", deviceCreatedDate = "Created: 28/9/2024"),
                LoggedDeviceInfo(deviceName = "Device 3", deviceCreatedDate = "Created: 28/9/2024"),
                LoggedDeviceInfo(deviceName = "Device 4", deviceCreatedDate = "Created: 28/9/2024"),
                LoggedDeviceInfo(deviceName = "Device 5", deviceCreatedDate = "Created: 28/9/2024")
            )
        )
    }
}
