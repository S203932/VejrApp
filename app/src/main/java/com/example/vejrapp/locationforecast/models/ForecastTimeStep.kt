package com.example.vejrapp.locationforecast.models

import com.google.gson.annotations.SerializedName

// Forecast for a specific time step
data class ForecastTimeStep(
    @SerializedName("data") val data: ForecastTimeStepData, // Forecast for a specific time
    @SerializedName("time") val time: String // The time these forecast values are valid for. Timestamp in format YYYY-MM-DDThh:mm:ssZ (ISO 8601)
)