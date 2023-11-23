package com.example.vejrapp.data.remote.locationforecast

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.Status
import retrofit2.http.GET
import retrofit2.http.Query

interface Locationforecast {
    @GET("complete")
    suspend fun getComplete(
        @Query("altitude") altitude: Int? = null,
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): METJSONForecastTimestamped

    @GET("status")
    suspend fun getStatus(): Status
}