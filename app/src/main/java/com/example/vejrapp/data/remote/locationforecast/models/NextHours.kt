package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

// Used for deserialization of the API data
data class NextHours(
    @SerializedName("details") val details: ForecastTimePeriod,
    @SerializedName("summary") val summary: ForecastSummary?
)