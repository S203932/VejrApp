package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

// Forecast for a specific time
data class ForecastTimeStepData(
    @SerializedName("instant") val instant: NextHours? = null, // Parameters which applies to this exact point in time
    @SerializedName("next_12_hours") val nextTwelveHours: NextHours? = null, // Parameters with validity times over twelve hours. Will not exist for all time steps.
    @SerializedName("next_1_hours") val nextOneHours: NextHours? = null, // Parameters with validity times over one hour. Will not exist for all time steps.
    @SerializedName("next_6_hours") val nextSixHours: NextHours? = null, // Parameters with validity times over six hours. Will not exist for all time steps.
)
