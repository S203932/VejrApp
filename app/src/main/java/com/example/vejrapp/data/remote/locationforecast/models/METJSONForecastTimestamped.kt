package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

data class METJSONForecastTimestamped(
    @SerializedName("met_json_forecast") val metJsonForecast: METJSONForecast,
    @SerializedName("expires") val expires: String,
    @SerializedName("last_modified") val lastModified: String
)