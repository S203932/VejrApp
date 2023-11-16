package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast

// More usable version of METJSONForecast
class CurrentWeather(complete: METJSONForecast) {
    // Top half of dayPage information
    private val currentWeather = complete.properties.timeseries[0]
    val currentTemperature = currentWeather.data.instant?.details?.airTemperature
    val currentCondition = currentWeather.data.nextOneHours?.summary?.symbolCode

    // TODO replace with real calculated realfeel
    val realFeel = currentWeather.data.instant?.details?.dewPointTemperature
    val currentMinTemperature = currentWeather.data.nextOneHours?.details?.airTemperatureMin
    val currentMaxTemperature = currentWeather.data.nextOneHours?.details?.airTemperatureMax
    val currentPercentageRain =
        currentWeather.data.nextOneHours?.details?.probabilityOfPrecipitation
    val currentWindSpeed = currentWeather.data.instant?.details?.windSpeed


    // No weather caution information in API data
    // To make weather caution one must analyze the data oneself and issue warnings accordingly


    // Hourly Data
    val hourlyTemperature = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[index].data.instant?.details?.airTemperature
    }
    val hourlyCondition = MutableList<String>(24) { index ->
        complete.properties.timeseries[index].data.nextOneHours?.summary?.symbolCode.toString()
    }

    val hourlyPercentageRain = MutableList<Float?>(24) { index ->
        complete.properties.timeseries[index].data.nextOneHours?.details?.probabilityOfPrecipitation
    }

    // The middle of DayPage
    // There is no visibility in API Data, can only say how much fog
    val humidity = currentWeather.data.instant?.details?.relativeHumidity
    val uVIndex = currentWeather.data.instant?.details?.ultravioletIndexClearSky
    val pressure = currentWeather.data.instant?.details?.airPressureAtSeaLevel

}