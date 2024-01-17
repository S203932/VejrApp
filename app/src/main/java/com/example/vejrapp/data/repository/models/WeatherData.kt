package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.remote.locationforecast.models.Forecast
import com.example.vejrapp.data.remote.locationforecast.models.ForecastMeta
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.NextHours
import com.example.vejrapp.data.repository.WeatherUtils.applyTimezone
import com.example.vejrapp.data.repository.WeatherUtils.celsiusToFahrenheit
import com.example.vejrapp.data.repository.WeatherUtils.msToKmH
import com.example.vejrapp.data.repository.WeatherUtils.roundFloat
import com.example.vejrapp.ui.settings.models.SettingsModel
import java.time.ZoneId
import java.util.TimeZone

// More usable version of METJSONForecast
data class WeatherData(
    val metjsonForecastTimestamped: METJSONForecastTimestamped,
    val city: City,
    val settings: SettingsModel
) {

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
        val existingUnits = metjsonForecast.properties.meta.units
        val newUnits = existingUnits.copy(
            // Pressure
            airPressureAtSeaLevel = if (settings.pressureSetting.checked) {
                settings.pressureSetting.choiceUnit
            } else {
                existingUnits.airPressureAtSeaLevel
            },
            // Temperature
            airTemperature = if (settings.temperatureSetting.checked) {
                settings.temperatureSetting.choiceUnit
            } else {
                existingUnits.airTemperature
            },
            airTemperatureMax = if (settings.temperatureSetting.checked) {
                settings.temperatureSetting.choiceUnit
            } else {
                existingUnits.airTemperatureMax
            },
            airTemperatureMin = if (settings.temperatureSetting.checked) {
                settings.temperatureSetting.choiceUnit
            } else {
                existingUnits.airTemperatureMin
            },
            // Wind speed
            windSpeed = if (settings.windSpeedSetting.checked) {
                settings.windSpeedSetting.choiceUnit
            } else {
                existingUnits.windSpeed
            },
            windSpeedOfGust = if (settings.windSpeedSetting.checked) {
                settings.windSpeedSetting.choiceUnit
            } else {
                existingUnits.windSpeedOfGust
            }
        )

        fun convertNextHours(nextHours: NextHours?): NextHours? {
            return if (nextHours != null) {
                NextHours(
                    details = nextHours.details.copy(
                        // Pressure
                        airPressureAtSeaLevel = convertPressure(nextHours.details.airPressureAtSeaLevel),
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
        if (settings.temperatureSetting.checked && temperature != null) {
            return roundFloat(celsiusToFahrenheit(temperature)!!)
        }
        return temperature
    }

    private fun convertWindSpeed(windSpeed: Float?): Float? {
        if (settings.windSpeedSetting.checked && windSpeed != null) {
            return roundFloat(msToKmH(windSpeed)!!)
        }
        return windSpeed
    }

    private fun convertPressure(pressure: Float?): Float? {
        // Calculation from hPa -> atm
        // Source https://en.wikipedia.org/wiki/Standard_atmosphere_(unit)
        if (settings.pressureSetting.checked && pressure != null) {
            return roundFloat((pressure / 10000) * 9.8692F, decimalPlaces = 4)
        }
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

