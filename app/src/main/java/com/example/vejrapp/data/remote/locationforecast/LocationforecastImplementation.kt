package com.example.vejrapp.data.remote.locationforecast

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.Status
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationforecastImplementation : Locationforecast {

    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiClient: Locationforecast = retrofit.create(Locationforecast::class.java)

    override suspend fun getComplete(
        altitude: Int?,
        latitude: Float,
        longitude: Float
    ): METJSONForecast {
        return apiClient.getComplete(altitude, latitude, longitude)
    }

    override suspend fun getStatus(): Status {
        return apiClient.getStatus()
    }
}