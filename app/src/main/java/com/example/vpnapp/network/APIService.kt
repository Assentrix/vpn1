package com.example.vpnapp.network

import com.example.vpnapp.ui.dummy.model.ExampleData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("example-endpoint")
    suspend fun getExampleData(): Response<ExampleData>
}
