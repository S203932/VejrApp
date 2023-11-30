package com.example.vejrapp.data.remote.locationforecast.models
import com.google.gson.annotations.SerializedName
// Used for deserialization of the API data
// GeoJSON point type
data class PointGeometry(
    @SerializedName("coordinates") val coordinates: List<Float>, // [longitude, latitude, altitude]. All numbers in decimal.
    @SerializedName("type") val type: PointGeometryEnum
)