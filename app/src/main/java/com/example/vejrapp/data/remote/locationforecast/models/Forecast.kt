package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

// Used for deserilization of API data
data class Forecast(
    @SerializedName("meta") val meta: ForecastMeta,
    @SerializedName("timeseries") val timeseries: List<ForecastTimeStep> // Forecast timeseries

)