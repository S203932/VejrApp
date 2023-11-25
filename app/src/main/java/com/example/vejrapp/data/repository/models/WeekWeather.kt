package com.example.vejrapp.data.repository.models

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import java.time.LocalDate
import java.time.Period

class WeekWeather(complete: METJSONForecastTimestamped) {

    private val weatherData = complete.metJsonForecast.properties.timeseries


    private val day1 = weatherData[dateCalculator(1)]
    private val day2 = weatherData[dateCalculator(2)]
    private val day3 = weatherData[dateCalculator(3)]
    private val day4 = weatherData[dateCalculator(4)]
    private val day5 = weatherData[dateCalculator(5)]
    private val day6 = weatherData[dateCalculator(6)]
    private val day7 = weatherData[dateCalculator(7)]

    // Tomorrow
    val day1Temp = day1.data.instant?.details?.airTemperature
    val day1Min = calculateMin(complete.metJsonForecast, 1)
    val day1Max = calculateMax(complete.metJsonForecast, 1)
    val day1Condition = day1.data.nextTwelveHours?.summary?.symbolCode
    val day1Rain = day1.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day1Humidity = day1.data.instant?.details?.relativeHumidity
    val day1Date = day1.time.toLocalDate()
    val day1Time = day1.time.hour

    // Day2
    val day2Temp = day2.data.instant?.details?.airTemperature
    val day2Min = calculateMin(complete.metJsonForecast, 2)
    val day2Max = calculateMax(complete.metJsonForecast, 2)
    val day2Condition = day2.data.nextTwelveHours?.summary?.symbolCode
    val day2Rain = day2.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day2Humidity = day2.data.instant?.details?.relativeHumidity
    val day2Date = day2.time.toLocalDate()
    val day2Time = day2.time.hour

    // Day3
    val day3Temp = day3.data.instant?.details?.airTemperature
    val day3Min = calculateMin(complete.metJsonForecast, 3)
    val day3Max = calculateMax(complete.metJsonForecast, 3)
    val day3Condition = day3.data.nextTwelveHours?.summary?.symbolCode
    val day3Rain = day3.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day3Humidity = day3.data.instant?.details?.relativeHumidity
    val day3Date = day3.time.toLocalDate()
    val day3Time = day3.time.hour

    // Day4
    val day4Temp = day4.data.instant?.details?.airTemperature
    val day4Min = calculateMin(complete.metJsonForecast, 4)
    val day4Max = calculateMax(complete.metJsonForecast, 4)
    val day4Condition = day4.data.nextTwelveHours?.summary?.symbolCode
    val day4Rain = day4.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day4Humidity = day4.data.instant?.details?.relativeHumidity
    val day4Date = day4.time.toLocalDate()
    val day4Time = day4.time.hour

    // Day5
    val day5Temp = day5.data.instant?.details?.airTemperature
    val day5Min = calculateMin(complete.metJsonForecast, 5)
    val day5Max = calculateMax(complete.metJsonForecast, 5)
    val day5Condition = day5.data.nextTwelveHours?.summary?.symbolCode
    val day5Rain = day5.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day5Humidity = day5.data.instant?.details?.relativeHumidity
    val day5Date = day5.time.toLocalDate()
    val day5Time = day5.time.hour

    // Day6
    val day6Temp = day6.data.instant?.details?.airTemperature
    val day6Min = calculateMin(complete.metJsonForecast, 6)
    val day6Max = calculateMax(complete.metJsonForecast, 6)
    val day6Condition = day6.data.nextTwelveHours?.summary?.symbolCode
    val day6Rain = day6.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day6Humidity = day6.data.instant?.details?.relativeHumidity
    val day6Date = day6.time.toLocalDate()
    val day6Time = day6.time.hour

    // Day7
    val day7Temp = day7.data.instant?.details?.airTemperature
    val day7Min = calculateMin(complete.metJsonForecast, 7)
    val day7Max = calculateMax(complete.metJsonForecast, 7)
    val day7Condition = day7.data.nextTwelveHours?.summary?.symbolCode
    val day7Rain = day7.data.nextTwelveHours?.details?.probabilityOfPrecipitation
    val day7Humidity = day7.data.instant?.details?.relativeHumidity
    val day7Date = day7.time.toLocalDate()
    val day7Time = day7.time.hour


    fun dateCalculator(day: Int): Int {
        var n = 0;
        val currentDay = LocalDate.now()
        var temp = LocalDate.now().atTime(12, 0, 0)
        var time = temp.hour
        var tempDay = weatherData.get(n).time.toLocalDate()
        var tempTime = weatherData.get(n).time.hour

        while (Period.between(currentDay, tempDay).days != day || tempTime != time) {
            n += 1
            tempDay = weatherData.get(n).time.toLocalDate()
            tempTime = weatherData.get(n).time.hour
        }

        return n
    }

    fun calculateMin(complete: METJSONForecast, day: Int): Float {
        var n = 0;
        var lowTemp = 1000F
        val currentDay = LocalDate.now()
        var tempDay = weatherData.get(n).time.toLocalDate()

        while (Period.between(currentDay, tempDay).days != day) {
            n += 1
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        while (Period.between(currentDay, tempDay).days == day) {
            if (weatherData.get(n).data.instant?.details?.airTemperature ?: 3000f < lowTemp) {
                lowTemp = weatherData.get(n).data.instant?.details?.airTemperature ?: 1000F
            }
            n++
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        return lowTemp
    }

    fun calculateMax(complete: METJSONForecast, day: Int): Float {
        var n = 0;
        var lowTemp = -1000F
        val currentDay = LocalDate.now()
        var tempDay = weatherData.get(n).time.toLocalDate()

        while (Period.between(currentDay, tempDay).days != day) {
            n += 1
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        while (Period.between(currentDay, tempDay).days == day) {
            if (weatherData.get(n).data.instant?.details?.airTemperature ?: -3000f > lowTemp) {
                lowTemp = weatherData.get(n).data.instant?.details?.airTemperature ?: -1000F
            }
            n++
            tempDay = weatherData.get(n).time.toLocalDate()
        }

        return lowTemp
    }

}