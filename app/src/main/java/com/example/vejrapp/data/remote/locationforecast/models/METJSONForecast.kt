package com.example.vejrapp.data.remote.locationforecast.models
import com.google.gson.annotations.SerializedName

// Used for deserialization of the API data
data class METJSONForecast(
    @SerializedName("geometry") val geometry: PointGeometry,
    @SerializedName("properties") val properties: Forecast,
    @SerializedName("type") val type: METJSONForecastEnum
)