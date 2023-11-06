package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

data class ForecastUnits(
    @SerializedName("air_pressure_at_sea_level") val airPressureAtSeaLevel: String? = null, // hPa
    @SerializedName("air_temperature") val airTemperature: String? = null, // C
    @SerializedName("air_temperature_max") val airTemperatureMax: String? = null, // C
    @SerializedName("air_temperature_min") val airTemperatureMin: String? = null, // C
    @SerializedName("cloud_area_fraction") val cloudAreaFraction: String? = null, // %
    @SerializedName("cloud_area_fraction_high") val cloudAreaFractionHigh: String? = null, // %
    @SerializedName("cloud_area_fraction_low") val cloudAreaFractionLow: String? = null, // %
    @SerializedName("cloud_area_fraction_medium") val cloudAreaFractionMedium: String? = null, // %
    @SerializedName("dew_point_temperature") val dewPointTemperature: String? = null, // C
    @SerializedName("fog_area_fraction") val fogAreaFraction: String? = null, // %
    @SerializedName("precipitation_amount") val precipitationAmount: String? = null, // mm
    @SerializedName("precipitation_amount_max") val precipitationAmountMax: String? = null, // mm
    @SerializedName("precipitation_amount_min") val precipitationAmountMin: String? = null, // mm
    @SerializedName("probability_of_precipitation") val probabilityOfPrecipitation: String? = null, // %
    @SerializedName("probability_of_thunder") val probabilityOfThunder: String? = null, // %
    @SerializedName("relative_humidity") val relativeHumidity: String? = null, // %
    @SerializedName("ultraviolet_index_clear_sky") val ultravioletIndexClearSky: String? = null,
    @SerializedName("wind_from_direction") val windFromDirection: String? = null, // degrees
    @SerializedName("wind_speed") val windSpeed: String? = null, // m/s
    @SerializedName("wind_speed_of_gust") val windSpeedOfGust: String? = null // m/s
)
