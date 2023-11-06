package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

// Forecast for a specific time step
data class ForecastTimeStep(
    @SerializedName("data") val data: ForecastTimeStepData, // Forecast for a specific time
    @SerializedName("time") val time: ZonedDateTime // The time these forecast values are valid for. Timestamp in format YYYY-MM-DDThh:mm:ssZ (ISO 8601)
)