package com.example.vejrapp.data.local.default

import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.remote.locationforecast.models.Forecast
import com.example.vejrapp.data.remote.locationforecast.models.ForecastMeta
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStep
import com.example.vejrapp.data.remote.locationforecast.models.ForecastTimeStepData
import com.example.vejrapp.data.remote.locationforecast.models.ForecastUnits
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastEnum
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometry
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometryEnum
import com.example.vejrapp.data.repository.models.CurrentWeather
import com.example.vejrapp.data.repository.models.WeekWeather
import java.time.ZonedDateTime

object DefaultData {
    private val copenhagen = City(
        name = "Copenhagen",
        country = "Denmark",
        latitude = 55.67594F,
        longitude = 12.56553F,
        population = 1153615

    )
    private val sofia = City(
        name = "Sofia",
        country = "Bulgaria",
        latitude = 42.69751F,
        longitude = 23.32415F,
        population = 1152556
    )
    val defaultCity: City = copenhagen

    val cities = listOf<City>(copenhagen, sofia)

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

    val defaultCurrentWeather = CurrentWeather(defaultComplete)

    // Added weekWeather
    val defaultWeekWeather = WeekWeather(defaultComplete)
}