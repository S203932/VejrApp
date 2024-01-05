package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import java.time.ZoneId
import java.time.ZonedDateTime

// More usable version of METJSONForecast
class WeatherData(metjsonForecastTimestamped: METJSONForecastTimestamped, val city: City) {

    /// Get weather data, expire date and data timestamp
    private val complete = metjsonForecastTimestamped.metJsonForecast
    val expires = metjsonForecastTimestamped.expires
    val units = complete.properties.meta.units
    val updatedAt = complete.properties.meta.updatedAt

    private var weatherData = complete.properties.timeseries
    val data = Data()

    // What has been done
    // - Create a day object which contains an array of timeseries data
    // - Create a data object which contains an array of day objects
    // - Sort all the data from timeseries into day objects.
    // - For each day object created in needs to be added to the data object.


    // Day object to contain all data from a day
    data class Day(var hours: MutableList<ForecastTimeStep> = mutableListOf<ForecastTimeStep>()) {
    }

    // Data object that should contain a list of Day objects
    data class Data(var days: MutableList<Day> = mutableListOf<Day>()) {
    }


    // Now I need to sort all the data from weatherData/TimeSeries into Data class
    private fun sortData() {

        if (weatherData[0].time.zone != ZoneId.of(city.timezone)) {
            for (item in weatherData) {
                item.time = applyTimezone(item.time, city)
            }
        }


        var day = Day()
        day.hours.add(weatherData[0])
        for (item in weatherData.subList(1, weatherData.size)) {
            if (item.time.dayOfMonth == day.hours[0].time.dayOfMonth) {
                day.hours.add(item)
            } else {
                data.days.add(day)
                day = Day()
                day.hours.add(item)
            }
        }
        data.days.add(day)

    }

    private val functionCall = sortData()

    // Method to apply a time zone
    fun applyTimezone(zonedDateTime: ZonedDateTime, city: City): ZonedDateTime {
        return zonedDateTime.withZoneSameInstant(ZoneId.of(city.timezone))
    }

}

