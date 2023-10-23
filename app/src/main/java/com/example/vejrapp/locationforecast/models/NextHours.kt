package com.example.vejrapp.locationforecast.models

import com.google.gson.annotations.SerializedName

data class NextHours(
    @SerializedName("details") val details: ForecastTimePeriod,
    @SerializedName("summary") val summary: ForecastSummary
)
