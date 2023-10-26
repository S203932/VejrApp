package com.example.vejrapp.locationforecast.models

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("meta") val meta: ForecastMeta,
    @SerializedName("timeseries") val timeseries: List<ForecastTimeStep> // Forecast timeseries

)