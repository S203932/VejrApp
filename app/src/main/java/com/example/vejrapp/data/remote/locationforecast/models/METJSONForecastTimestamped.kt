package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class METJSONForecastTimestamped(
    @SerializedName("met_json_forecast") val metJsonForecast: METJSONForecast,
    @SerializedName("expires") val expires: ZonedDateTime,
    @SerializedName("last_modified") val lastModified: ZonedDateTime,
)