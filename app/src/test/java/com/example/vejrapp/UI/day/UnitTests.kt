package com.example.vejrapp.UI.day

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
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class UnitTests {

    private val complete = METJSONForecastTimestamped(METJSONForecast(
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
    ), ZonedDateTime.now(), ZonedDateTime.now())

    private val current = CurrentWeather(complete)
    private val week = WeekWeather(complete)

    @Test
    fun prettyDateTest() {
        val date = ZonedDateTime.of(2001, 3, 11, 12, 13, 14, 15, ZoneId.of("UTC"))
        assert(prettyDate(date) == "Sunday, March 11.")
    }

    @Test
    fun prettyTimeTest() {
        val date = ZonedDateTime.of(2001, 3, 11, 12, 13, 14, 15, ZoneId.of("UTC"))
        assert(prettyTime(date) == "12:13")
    }

    @Test
    fun calculateTimeDataTest() {
        assert(current.currentTimeData(complete.metJsonForecast) == 0)

    }

    @Test
    fun calculateMinTest() {
        assert(current.calculateMin(complete.metJsonForecast, 1).equals(1000F))
        assert(week.calculateMin(complete.metJsonForecast, 1).equals(1000F))
    }

    @Test
    fun calculateMaxTest() {
        assert(current.calculateMax(complete.metJsonForecast, 1).equals(-1000F))
        assert(week.calculateMax(complete.metJsonForecast, 1).equals(-1000F))
    }
}

