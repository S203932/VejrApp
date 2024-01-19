package com.example.vejrapp.ui.day

/*
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

    private val current = WeatherData(complete)
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


 */
