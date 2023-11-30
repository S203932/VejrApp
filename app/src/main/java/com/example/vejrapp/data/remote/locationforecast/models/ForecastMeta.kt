package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

// Used for desrelization of the API data
data class ForecastMeta(
    @SerializedName("units") val units: ForecastUnits,
    @SerializedName("updated_at") val updatedAt: ZonedDateTime // Update time for this forecast.
)