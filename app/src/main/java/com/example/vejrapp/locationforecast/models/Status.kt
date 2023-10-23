package com.example.vejrapp.locationforecast.models

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("last_update") val lastUpdate: String,
)