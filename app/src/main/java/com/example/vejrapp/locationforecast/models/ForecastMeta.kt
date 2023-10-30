package com.example.vejrapp.locationforecast.models

import com.google.gson.annotations.SerializedName

data class ForecastMeta(
    @SerializedName("units") val units: ForecastUnits,
    @SerializedName("updated_at") val updatedAt: String // Update time for this forecast.
)