package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class ForecastMeta(
    @SerializedName("units") val units: ForecastUnits,
    @SerializedName("updated_at") val updatedAt: ZonedDateTime // Update time for this forecast.
)