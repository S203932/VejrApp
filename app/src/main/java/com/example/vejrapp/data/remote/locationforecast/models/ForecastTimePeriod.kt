package com.example.vejrapp.data.remote.locationforecast.models

import com.google.gson.annotations.SerializedName

// Used for desrelization of the API data
// Weather parameters valid for a specified time period.
data class ForecastTimePeriod(
    @SerializedName("air_pressure_at_sea_level") val airPressureAtSeaLevel: Float? = null, // Air pressure at sea level
    @SerializedName("air_temperature_max") val airTemperatureMax: Float? = null, // Maximum air temperature in period
    @SerializedName("air_temperature_min") val airTemperatureMin: Float? = null, // Minimum air temperature in period
    @SerializedName("air_temperature_percentile_10") val airTemperaturePercentileTen: Float? = null, // Air temperature
    @SerializedName("air_temperature_percentile_90") val airTemperaturePercentileNinety: Float? = null, // Air temperature
    @SerializedName("air_temperature") val airTemperature: Float? = null, // Air temperature
    @SerializedName("cloud_area_fraction_high") val cloudAreaFractionHigh: Float? = null, // Amount of sky covered by clouds at high elevation.
    @SerializedName("cloud_area_fraction_low") val cloudAreaFractionLow: Float? = null, // Amount of sky covered by clouds at low elevation.
    @SerializedName("cloud_area_fraction_medium") val cloudAreaFractionMedium: Float? = null, // Amount of sky covered by clouds at medium elevation.
    @SerializedName("cloud_area_fraction") val cloudAreaFraction: Float? = null, // Amount of sky covered by clouds.
    @SerializedName("dew_point_temperature") val dewPointTemperature: Float? = null, // Dew point temperature at sea level
    @SerializedName("fog_area_fraction") val fogAreaFraction: Float? = null, // Amount of area covered by fog.
    @SerializedName("precipitation_amount_max") val precipitationAmountMax: Float? = null, // Maximum amount of precipitation for this period
    @SerializedName("precipitation_amount_min") val precipitationAmountMin: Float? = null, // Minimum amount of precipitation for this period
    @SerializedName("precipitation_amount") val precipitationAmount: Float? = null, // Best estimate for amount of precipitation for this period
    @SerializedName("probability_of_precipitation") val probabilityOfPrecipitation: Float? = null, // Probability of any precipitation coming for this period
    @SerializedName("probability_of_thunder") val probabilityOfThunder: Float? = null, // Probability of any thunder coming for this period
    @SerializedName("relative_humidity") val relativeHumidity: Float? = null, // Amount of humidity in the air.
    @SerializedName("ultraviolet_index_clear_sky") val ultravioletIndexClearSky: Float? = null, // Maximum ultraviolet index if sky is clear
    @SerializedName("wind_from_direction") val windFromDirection: Float? = null, // The direction which moves towards.
    @SerializedName("wind_speed_of_gust") val windSpeedOfGust: Float? = null, // Speed of wind gust.
    @SerializedName("wind_speed_percentile_10") val windSpeedPercentileTen: Float? = null, // Speed of wind gust.
    @SerializedName("wind_speed_percentile_90") val windSpeedPercentileNinety: Float? = null, // Speed of wind gust.
    @SerializedName("wind_speed") val windSpeed: Float? = null, // Speed of wind.
)
