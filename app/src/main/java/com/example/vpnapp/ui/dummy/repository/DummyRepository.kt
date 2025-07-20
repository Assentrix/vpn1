package com.example.vpnapp.ui.dummy.repository

import com.example.vpnapp.network.ApiService
import com.example.vpnapp.ui.dummy.model.ExampleData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getExampleData(): Result<ExampleData> {
        return try {
            val response = apiService.getExampleData()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
