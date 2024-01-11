package com.example.vejrapp.data.repository

import com.example.vejrapp.data.repository.models.WeatherData
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

object WeatherUtils {

    // Method to apply a time zone to a ZonedDateTime
    fun applyTimezone(zonedDateTime: ZonedDateTime, timeZone: TimeZone): ZonedDateTime {
        return zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone.id))
    }

    // Method to calculate the max temperature of the current day
    fun calculateMaxTemperature(weatherData: WeatherData): Float {
        var maxTemp = -1000F
        for (item in weatherData.data.days[0].hours) {
            if ((item.data.instant?.details?.airTemperature ?: -3000f) > maxTemp) {
                maxTemp = item.data.instant?.details?.airTemperature ?: -1000F
            }
        }
        return maxTemp
    }

    // Method to calculate the min temperature of the current day
    fun calculateMinTemperature(weatherData: WeatherData): Float {
        var minTemp = 1000F
        for (item in weatherData.data.days[0].hours) {
            if ((item.data.instant?.details?.airTemperature ?: 3000f) < minTemp) {
                minTemp = item.data.instant?.details?.airTemperature ?: 1000F
            }
        }
        return minTemp
    }
}