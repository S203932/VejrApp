package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

// A identifier that sums up the weather condition for this time period, see documentation.
data class ForecastSummary(
    @SerializedName("symbol_code") val symbolCode: WeatherSymbol,
    @SerializedName("symbol_confidence") val symbolConfidence: String? = null
)