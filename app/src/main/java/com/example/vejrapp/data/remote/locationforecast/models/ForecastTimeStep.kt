package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

// Used for desrelization of the API data
// Forecast for a specific time step
data class ForecastTimeStep(
    @SerializedName("data") val data: ForecastTimeStepData, // Forecast for a specific time
    @SerializedName("time") var time: ZonedDateTime // The time these forecast values are valid for. Timestamp in format YYYY-MM-DDThh:mm:ssZ (ISO 8601)
)