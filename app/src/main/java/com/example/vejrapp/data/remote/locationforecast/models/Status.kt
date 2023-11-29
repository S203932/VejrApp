package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class Status(
    @SerializedName("last_update") val lastUpdate: ZonedDateTime,
)