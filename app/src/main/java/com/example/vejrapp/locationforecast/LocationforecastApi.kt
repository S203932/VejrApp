package com.example.vejrapp.locationforecast

import com.example.vejrapp.locationforecast.models.METJSONForecast
import com.example.vejrapp.locationforecast.models.Status
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LocationforecastApi {
    @Headers("User-Agent: VejrApp https://github.com/S203932/VejrApp")
    @GET("complete")
    fun getComplete(
        @Query("altitude") altitude: Int? = null,
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): Call<METJSONForecast>

    @GET("status")
    fun getStatus(): Call<Status>
}