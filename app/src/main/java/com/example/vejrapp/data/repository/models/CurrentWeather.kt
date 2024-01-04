package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.math.exp

// More usable version of METJSONForecast
class CurrentWeather(metjsonForecastTimestamped: METJSONForecastTimestamped, val city: City) {

    // Get weather data, expire date and data timestamp
    private val complete = metjsonForecastTimestamped.metJsonForecast
    val expires = metjsonForecastTimestamped.expires

    val units = complete.properties.meta.units
    private val weatherData = complete.properties.timeseries


    // Top half of dayPage information
    private val currentWeather = complete.properties.timeseries[currentTimeData(complete)]
    val currentTemperature = currentWeather.data.instant?.details?.airTemperature
    val currentCondition = currentWeather.data.nextOneHours?.summary?.symbolCode
    val updatedAt = complete.properties.meta.updatedAt

    val currentMinTemperature = calculateMin(complete, 0)
    val currentMaxTemperature = calculateMax(complete, 0)
    val currentPercentageRain =
        currentWeather.data.nextOneHours?.details?.probabilityOfPrecipitation
    val currentWindSpeed = currentWeather.data.instant?.details?.windSpeed

    // No weather caution information in API data
    // To make weather caution one must analyze the data oneself and issue warnings accordingly

    // Hourly Data after the current time (from the next hour and forward 24 hours)
    val hourlyTemperature = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[currentTimeData(complete) + index].data.instant?.details?.airTemperature
    }
    val hourlyCondition = MutableList<String>(24) { index ->
        complete.properties.timeseries[currentTimeData(complete) + index].data.nextOneHours?.summary?.symbolCode.toString()
    }

    val hourlyPercentageRain = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[currentTimeData(complete) + index].data.nextOneHours?.details?.probabilityOfPrecipitation
    }

    val hourlyPercentageRainDumbMin = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[currentTimeData(complete) + index].data.nextOneHours?.details?.precipitationAmountMin
    }

    val hourlyPercentageRainDumbMax = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[currentTimeData(complete) + index].data.nextOneHours?.details?.precipitationAmountMax
    }
    // The middle of DayPage
    val humidity = currentWeather.data.instant?.details?.relativeHumidity
    val thunder = currentWeather.data.instant?.details?.probabilityOfThunder
    val uvIndex = currentWeather.data.instant?.details?.ultravioletIndexClearSky
    val pressure = currentWeather.data.instant?.details?.airPressureAtSeaLevel

    val feelsLike = calculateFeelsLike()

    private fun calculateFeelsLike(): Float? {
        // Calculated using Australian apparent temperature (https://en.wikipedia.org/wiki/Wind_chill)
        // TODO check if this should be changed
        return try {
            val e =
                (humidity!! / 100) * 6.105F * exp((17.27F * currentTemperature!!) / (237.7 + currentTemperature))
            val at = currentTemperature + (0.33F * e) - (0.70F * currentWindSpeed!!) - 4.00F
            // Round to 1 decimal place
            at.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toFloat()

        } catch (error: Exception) {
            null
        }
    }

    fun currentTimeData(complete: METJSONForecast): Int {
        var x = 0
        while (complete.properties.timeseries[x].time.toLocalTime().hour != LocalTime.now().hour) {
            x++
        }
        return x
    }

    fun calculateMin(complete: METJSONForecast, day: Int): Float {
        var n = 0;
        var lowTemp = 1000F
        val currentDay = LocalDate.now()
        var tempDay = weatherData.get(n).time.toLocalDate()

        while (Period.between(currentDay, tempDay).days != day) {
            n += 1
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        while (Period.between(currentDay, tempDay).days == day) {
            if (weatherData.get(n).data.instant?.details?.airTemperature ?: 3000f < lowTemp) {
                lowTemp = weatherData.get(n).data.instant?.details?.airTemperature ?: 1000F
            }
            n++
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        return lowTemp
    }

    fun calculateMax(complete: METJSONForecast, day: Int): Float {
        var n = 0;
        var lowTemp = -1000F
        val currentDay = LocalDate.now()
        var tempDay = weatherData.get(n).time.toLocalDate()

        while (Period.between(currentDay, tempDay).days != day) {
            n += 1
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        while (Period.between(currentDay, tempDay).days == day) {
            if (weatherData.get(n).data.instant?.details?.airTemperature ?: -3000f > lowTemp) {
                lowTemp = weatherData.get(n).data.instant?.details?.airTemperature ?: -1000F
            }
            n++
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        return lowTemp
    }

}

/*
@DrawableRes
fun String.mapToYRImageResource(): Int =
    when (this) {
        "clearsky_day" -> {
            R.drawable.clearsky_day
        }

        "clearsky_night" -> {
            R.drawable.clearsky_night
        }

        "clearsky_polartwilight" -> {
            R.drawable.clearsky_polartwilight
        }

        "fair_day" -> {
            R.drawable.fair_day
        }

        "fair_night" -> {
            R.drawable.fair_night
        }

        "fair_polartwilight" -> {
            R.drawable.fair_polartwilight
        }

        "lightssnowshowersandthunder_day" -> {
            R.drawable.lightrainshowersandthunder_day
        }

        "lightssnowshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssnowshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "lightsnowshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightsnowshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightsnowshowers_polartwilight" -> {
            R.drawable.lightsnowshowers_polartwilight
        }

        "heavyrainandthunder" -> {
            R.drawable.heavyrainandthunder
        }

        "heavysnowandthunder" -> {
            R.drawable.heavysnowandthunder
        }

        "rainandthunder" -> {
            R.drawable.rainandthunder
        }

        "heavysleetshowersandthunder_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowersandthunder_night" -> {
            R.drawable.heavysleetshowersandthunder_night
        }

        "heavysleetshowersandthunder_polartwilight" -> {
            R.drawable.heavysleetshowersandthunder_polartwilight
        }

        "heavysnow" -> {
            R.drawable.heavysnow
        }

        "heavyrainshowers_day" -> {
            R.drawable.heavyrainshowers_day
        }

        "heavyrainshowers_night" -> {
            R.drawable.heavyrainshowers_night
        }

        "heavyrainshowers_polartwilight" -> {
            R.drawable.heavyrainshowers_polartwilight
        }

        "lightsleet" -> {
            R.drawable.lightsleet
        }

        "heavyrain" -> {
            R.drawable.heavyrain
        }

        "lightrainshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightrainshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightrainshowers_polartwilight" -> {
            R.drawable.lightrainshowers_polartwilight
        }

        "heavysleetshowers_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowers_night" -> {
            R.drawable.heavysleetshowers_night
        }

        "heavysleetshowers_polartwilight" -> {
            R.drawable.heavysleetshowers_polartwilight
        }

        "lightsleetshowers_day" -> {
            R.drawable.lightsleetshowers_day
        }

        "lightsleetshowers_night" -> {
            R.drawable.lightsleetshowers_night
        }

        "lightsleetshowers_polartwilight" -> {
            R.drawable.lightsleetshowers_polartwilight
        }

        "snow" -> {
            R.drawable.snow
        }

        "heavyrainshowersandthunder_day" -> {
            R.drawable.heavyrainshowersandthunder_day
        }

        "heavyrainshowersandthunder_night" -> {
            R.drawable.heavyrainshowersandthunder_night
        }

        "heavyrainshowersandthunder_polartwilight" -> {
            R.drawable.heavyrainshowersandthunder_polartwilight
        }

        "snowshowers_day" -> {
            R.drawable.snowshowers_day
        }

        "snowshowers_night" -> {
            R.drawable.snowshowers_night
        }

        "snowshowers_polartwilight" -> {
            R.drawable.snowshowers_polartwilight
        }

        "fog" -> {
            R.drawable.fog
        }

        "snowshowersandthunder_day" -> {
            R.drawable.snowshowersandthunder_day
        }

        "snowshowersandthunder_night" -> {
            R.drawable.snowshowersandthunder_night
        }

        "snowshowersandthunder_polartwilight" -> {
            R.drawable.snowshowersandthunder_polartwilight
        }

        "lightsnowandthunder" -> {
            R.drawable.lightsnowandthunder
        }

        "heavysleetandthunder" -> {
            R.drawable.heavysleetandthunder
        }

        "lightrain" -> {
            R.drawable.lightrain
        }

        "rainshowersandthunder_day" -> {
            R.drawable.rainshowersandthunder_day
        }

        "rainshowersandthunder_night" -> {
            R.drawable.rainshowersandthunder_night
        }

        "rainshowersandthunder_polartwilight" -> {
            R.drawable.rainshowersandthunder_polartwilight
        }

        "rain" -> {
            R.drawable.rain
        }

        "lightsnow" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_day" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_night" -> {
            R.drawable.lightrainshowersandthunder_night
        }

        "lightrainshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "heavysleet" -> {
            R.drawable.heavysleet
        }

        "sleetandthunder" -> {
            R.drawable.sleetandthunder
        }

        "lightrainandthunder" -> {
            R.drawable.lightrainandthunder
        }

        "sleet" -> {
            R.drawable.sleet
        }

        "lightssleetshowersandthunder_day" -> {
            R.drawable.lightssleetshowersandthunder_day
        }

        "lightssleetshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssleetshowersandthunder_polartwilight" -> {
            R.drawable.lightssleetshowersandthunder_polartwilight
        }

        "lightsleetandthunder" -> {
            R.drawable.lightsleetandthunder
        }

        "partlycloudy_day" -> {
            R.drawable.partlycloudy_day
        }

        "partlycloudy_night" -> {
            R.drawable.partlycloudy_night
        }

        "partlycloudy_polartwilight" -> {
            R.drawable.partlycloudy_polartwilight
        }

        "sleetshowersandthunder_day" -> {
            R.drawable.sleetshowersandthunder_day
        }

        "sleetshowersandthunder_night" -> {
            R.drawable.sleetshowersandthunder_night
        }

        "sleetshowersandthunder_polartwilight" -> {
            R.drawable.sleetshowersandthunder_polartwilight
        }

        "rainshowers_day" -> {
            R.drawable.rainshowers_day
        }

        "rainshowers_night" -> {
            R.drawable.rainshowers_night
        }

        "rainshowers_polartwilight" -> {
            R.drawable.rainshowers_polartwilight
        }

        "snowandthunder" -> {
            R.drawable.snowandthunder
        }

        "sleetshowers_day" -> {
            R.drawable.sleetshowers_day
        }

        "sleetshowers_night" -> {
            R.drawable.sleetshowers_night
        }

        "sleetshowers_polartwilight" -> {
            R.drawable.sleetshowers_polartwilight
        }

        "cloudy" -> {
            R.drawable.cloudy
        }

        "heavysnowshowersandthunder_day" -> {
            R.drawable.heavysnowshowersandthunder_day
        }

        "heavysnowshowersandthunder_night" -> {
            R.drawable.heavysnowshowersandthunder_night
        }

        "heavysnowshowersandthunder_polartwilight" -> {
            R.drawable.heavysnowshowersandthunder_polartwilight
        }

        "heavysnowshowers_day" -> {
            R.drawable.heavysnowshowers_day
        }

        "heavysnowshowers_night" -> {
            R.drawable.heavysnowshowers_night
        }

        "heavysnowshowers_polartwilight" -> {
            R.drawable.heavysnowshowers_polartwilight
        }

        else -> {
            R.drawable.cloudy
        }
    }
*/