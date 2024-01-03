package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.math.exp

// More usable version of METJSONForecast
class CurrentWeather(metjsonForecastTimestamped: METJSONForecastTimestamped, val city: City) {

    // Get weather data, expire date and data timestamp
    private val complete = metjsonForecastTimestamped.metJsonForecast
    val expires = metjsonForecastTimestamped.expires

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

    // The middle of DayPage
    val humidity = currentWeather.data.instant?.details?.relativeHumidity
    val uvIndex = currentWeather.data.instant?.details?.ultravioletIndexClearSky
    val pressure = currentWeather.data.instant?.details?.airPressureAtSeaLevel

    val realFeel = calculateRealFeel()

    private fun calculateRealFeel(): Float? {
        // Calculated using Australian apparent temperature (https://en.wikipedia.org/wiki/Wind_chill)
        return try {
            val e =
                (humidity!! / 100) * 6.105F * exp((17.27F * currentTemperature!!) / (237.7F + currentTemperature))
            val at = currentTemperature + (0.33F * e) - (0.7F * currentWindSpeed!!) - 4F

            // Round to 1 decimal place
            "%.1f".format(at).toFloat()

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

