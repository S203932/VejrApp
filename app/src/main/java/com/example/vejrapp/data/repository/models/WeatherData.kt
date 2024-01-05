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


    /*
        // Top half of dayPage information
        private val weatherData = complete.properties.timeseries[currentTimeData(complete)]
        val currentTemperature = weatherData.data.instant?.details?.airTemperature
        val currentCondition = weatherData.data.nextOneHours?.summary?.symbolCode
        val updatedAt = complete.properties.meta.updatedAt

        val currentMinTemperature = calculateMin(complete, 0)
        val currentMaxTemperature = calculateMax(complete, 0)
        val currentPercentageRain =
            weatherData.data.nextOneHours?.details?.probabilityOfPrecipitation
        val currentWindSpeed = weatherData.data.instant?.details?.windSpeed

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
        val humidity = weatherData.data.instant?.details?.relativeHumidity
        val uvIndex = weatherData.data.instant?.details?.ultravioletIndexClearSky
        val pressure = weatherData.data.instant?.details?.airPressureAtSeaLevel
        val humidity = currentWeather.data.instant?.details?.relativeHumidity
        val thunder = currentWeather.data.instant?.details?.probabilityOfThunder
        val uvIndex = currentWeather.data.instant?.details?.ultravioletIndexClearSky
        val pressure = currentWeather.data.instant?.details?.airPressureAtSeaLevel

        val feelsLike = calculateFeelsLike()
     */
    // Temporary Outcomment realFeel as it uses values not initialized


    /*
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

     */


}

