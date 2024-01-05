package com.example.vejrapp.data.local.default

import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.models.Forecast
import com.example.vejrapp.data.remote.locationforecast.models.ForecastMeta
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStepData
import com.example.vejrapp.data.remote.locationforecast.models.ForecastUnits
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastEnum
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometry
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometryEnum
import com.example.vejrapp.data.repository.models.CurrentWeather
import com.example.vejrapp.data.repository.models.WeekWeather
import com.example.vejrapp.ui.settings.models.SettingsModel
import java.time.ZonedDateTime

// The default data used for the repository data, when the api call wasn't
// called yet or was unsuccessfull
class DefaultData {
    object LOCATIONS {
        private val copenhagen = City(
            country = "Denmark",
            latitude = 55.67594F,
            longitude = 12.56553F,
            name = "Copenhagen",
            population = 1153615,
            timezone = "Europe/Copenhagen"

        )
        private val sofia = City(
            country = "Bulgaria",
            latitude = 42.69751F,
            longitude = 23.32415F,
            name = "Sofia",
            population = 1152556,
            timezone = "Europe/Sofia"
        )
        val CITY: City = copenhagen
        val CITIES = listOf<City>(copenhagen, sofia)
        val SEARCH_MODE: Boolean = false
        val SEARCH_TEXT: String = ""
        val FAVORITE_CITIES = listOf<City>()
    }

    object LOCATIONFORECAST {

        val COMPLETE: METJSONForecastTimestamped = METJSONForecastTimestamped(METJSONForecast(
            geometry = PointGeometry(
                coordinates = listOf(0F, 0F, 0F),
                type = PointGeometryEnum.Point
            ),
            properties = Forecast(
                meta = ForecastMeta(units = ForecastUnits(), updatedAt = ZonedDateTime.now()),
                timeseries = List(200) {
                    ForecastTimeStep(
                        time = ZonedDateTime.now().plusHours(it.toLong()),
                        data = ForecastTimeStepData()
                    )
                }
            ),
            type = METJSONForecastEnum.Feature
        ), ZonedDateTime.now())

        val CURRENT_WEATHER = CurrentWeather(COMPLETE, LOCATIONS.CITY)

        // Added weekweather
        val WEEK_WEATHER = WeekWeather(COMPLETE)
    }

    val defaultComplete = METJSONForecast(
        geometry = PointGeometry(
            coordinates = listOf(0F, 0F, 0F),
            type = PointGeometryEnum.Point
        ),
        properties = Forecast(
            meta = ForecastMeta(units = ForecastUnits(), updatedAt = ZonedDateTime.now()),
            timeseries = List(200) {
                ForecastTimeStep(
                    time = ZonedDateTime.now().plusHours(it.toLong()),
                    data = ForecastTimeStepData()
                )
            }
        ),
        type = METJSONForecastEnum.Feature
    )

    object SETTINGS {
        val TEMPERATURE = SettingsModel(
            name = "Temperature",
            choices = mapOf(
                Pair(false, "Celsius"),
                Pair(true, "Fahrenheit")
            )
        )
        val WIND_SPEED = SettingsModel(
            name = "Wind speed",
            choices = mapOf(
                Pair(false, "m/s"),
                Pair(true, "km/h")
            )
        )
        val PRESSURE = SettingsModel(
            name = "Pressure",
            choices = mapOf(
                Pair(false, "Bar"),
                Pair(true, "Pa")
            )
        )
    }
}