package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.models.Forecast
import com.example.vejrapp.data.remote.locationforecast.models.ForecastMeta
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.NextHours
import com.example.vejrapp.data.repository.WeatherUtils.applyTimezone
import java.time.ZoneId
import java.util.TimeZone

// More usable version of METJSONForecast
data class WeatherData(val metjsonForecastTimestamped: METJSONForecastTimestamped, val city: City) {

    /// Get weather data, expire date and data timestamp
    private val complete = applyUnitSettings(metjsonForecastTimestamped.metJsonForecast)

    //    private val complete = convertUnits(metjsonForecastTimestamped.metJsonForecast)
    val expires = metjsonForecastTimestamped.expires
    val units = complete.properties.meta.units
    val updatedAt = complete.properties.meta.updatedAt

    private val weatherData = complete.properties.timeseries
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


    private fun applyUnitSettings(metjsonForecast: METJSONForecast): METJSONForecast {

        val newTimeseries = mutableListOf<ForecastTimeStep>()
        val newUnits = metjsonForecast.properties.meta.units.copy()


        fun convertNextHours(nextHours: NextHours?): NextHours? {
            return if (nextHours != null) {
                NextHours(
                    details = nextHours.details.copy(
                        // Temperature
                        airTemperature = convertTemperature(nextHours.details.airTemperature),
                        airTemperatureMax = convertTemperature(nextHours.details.airTemperatureMax),
                        airTemperatureMin = convertTemperature(nextHours.details.airTemperatureMin),
                        airTemperaturePercentileNinety = convertTemperature(nextHours.details.airTemperaturePercentileNinety),
                        airTemperaturePercentileTen = convertTemperature(nextHours.details.airTemperaturePercentileTen),
                        // Wind speed
                        windSpeed = convertWindSpeed(nextHours.details.windSpeed),
                        windSpeedOfGust = convertWindSpeed(nextHours.details.windSpeedOfGust),
                        windSpeedPercentileNinety = convertWindSpeed(nextHours.details.windSpeedPercentileNinety),
                        windSpeedPercentileTen = convertWindSpeed(nextHours.details.windSpeedPercentileTen),
                        // Pressure
                        airPressureAtSeaLevel = convertPressure(nextHours.details.airPressureAtSeaLevel),
                    ),
                    summary = nextHours.summary
                )
            } else {
                null
            }
        }

        // Apply any needed conversions on timeSeries
        metjsonForecast.properties.timeseries.forEach {
            // Add new converted timeSeries
            newTimeseries.add(
                it.copy(
                    data = it.data.copy(
                        instant = convertNextHours(it.data.instant),
                        nextTwelveHours = convertNextHours(it.data.nextTwelveHours),
                        nextOneHours = convertNextHours(it.data.nextOneHours),
                        nextSixHours = convertNextHours(it.data.nextSixHours),
                    ), time = it.time
                )
            )
        }

        // Create and return
        return METJSONForecast(
            geometry = metjsonForecast.geometry,
            type = metjsonForecast.type,
            properties = Forecast(
                meta = ForecastMeta(
                    units = newUnits,
                    updatedAt = metjsonForecast.properties.meta.updatedAt
                ),
                timeseries = newTimeseries.toList()
            )
        )
    }

    private fun convertTemperature(temperature: Float?): Float? {

        return temperature
    }

    private fun convertWindSpeed(windSpeed: Float?): Float? {
        return windSpeed
    }

    private fun convertPressure(pressure: Float?): Float? {
        return pressure
    }

    // Now I need to sort all the data from weatherData/TimeSeries into Data class
    private fun sortData() {

        if (weatherData[0].time.zone != ZoneId.of(city.timezone)) {
            for (item in weatherData) {
                item.time = applyTimezone(item.time, TimeZone.getTimeZone(city.timezone))
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
}

