package com.example.vejrapp.data.repository

import android.util.Log
import com.example.vejrapp.data.repository.models.WeatherData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.math.RoundingMode
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import javax.inject.Singleton

@Singleton
object WeatherUtils {

    // Gson object for the whole project.
    // Specifically for serializing and deserializing locationforecast
    val gson: Gson = GsonBuilder().setLenient()
        .registerTypeAdapter(
            ZonedDateTime::class.java,
            ZonedDateTimeDeserializer()
        )
        .registerTypeAdapter(
            ZonedDateTime::class.java,
            ZonedDateTimeSerializer()
        ).create()

    // Deserializer for ZonedDateTime
    private class ZonedDateTimeDeserializer() : JsonDeserializer<ZonedDateTime> {
        override fun deserialize(
            json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
        ): ZonedDateTime {

            val formatter = {
                if (json?.asString?.contains("Z") == true) {
                    DateTimeFormatter.ISO_ZONED_DATE_TIME
                } else {
                    DateTimeFormatter.RFC_1123_DATE_TIME
                }
            }
            return if (json?.asString != null) {
//            ZonedDateTime.parse(json.asString, formatter())
                ZonedDateTime.parse(json.asString, formatter())
            }
            // Default to the dawn of time if date can't be deserialized
            else {
                ZonedDateTime.parse("1970-01-01T00:00:00Z")
            }
        }
    }

    private class ZonedDateTimeSerializer() : JsonSerializer<ZonedDateTime> {
        override fun serialize(
            src: ZonedDateTime?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src!!.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        }
    }

    // Method to apply a time zone to a ZonedDateTime
    fun applyTimezone(zonedDateTime: ZonedDateTime, timeZone: TimeZone): ZonedDateTime {
        return zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone.id))
    }

    // Method to calculate the max temperature of the current day
    fun calculateMaxTemperature(weatherData: WeatherData, dayInt: Int): Float {
        var maxTemp = -1000F
        for (item in weatherData.data.days[dayInt].hours) {
            if ((item.data.instant?.details?.airTemperature ?: -3000f) > maxTemp) {
                maxTemp = item.data.instant?.details?.airTemperature ?: -1000F
            }
        }
        return maxTemp
    }

    // Method to calculate the min temperature of the current day
    fun calculateMinTemperature(weatherData: WeatherData, dayInt: Int): Float {
        var minTemp = 1000F
        for (item in weatherData.data.days[dayInt].hours) {
            if ((item.data.instant?.details?.airTemperature ?: 3000f) < minTemp) {
                minTemp = item.data.instant?.details?.airTemperature ?: 1000F
            }
        }
        return minTemp
    }


    // Method to get the index for the current hour in the current day
    fun getCurrentIndex(weatherData: WeatherData, dayInt: Int): Int {
        val currentHour = if (dayInt > 0) {
            //Set currentHour to 0 so that the day can show from 00:00 for tomorrow page
            0
        } else {
            // Set currentHour to the relevant current time at the given time zone
            applyTimezone(ZonedDateTime.now(), TimeZone.getTimeZone(weatherData.city.timezone)).hour
        }
        val hours = weatherData.data.days[dayInt].hours

        val currentIndex = hours.indexOf(hours.find { it.time.hour == currentHour })

        return if (currentIndex == -1) {
            Log.d(
                WeatherUtils.TAGS.WEATHER_DATA_TAG,
                "Invalid index $currentHour. Using 0 instead for ${weatherData.city.getVerboseName()}"
            )
            0
        } else {
            currentIndex
        }
    }

    fun getDayHours(
        weatherData: WeatherData,
        dayInt: Int,
        overflowHours: Boolean = false
    ): WeatherData.Day {
        val day = WeatherData.Day()
        // adding all hours from the first day as it there can never be more than 24 hours in a day
        for (item in weatherData.data.days[dayInt].hours.subList(
            if (dayInt == 0) {
                getCurrentIndex(weatherData, dayInt)
            } else {
                0
            },
            weatherData.data.days[dayInt].hours.size
        )) {
            day.hours.add(item)
        }

        // Display 24 hours from now
        if (overflowHours) {
            // adding the remaining hours from the tomorrow to reach 24 hours
            for (item in weatherData.data.days[dayInt + 1].hours.subList(0, 24 - day.hours.size)) {
                day.hours.add(item)
            }
        }

        return day
    }

    // Round a float to a given amount of decimal places
    fun roundFloat(number: Float, decimalPlaces: Int = 1): Float {
        return number.toBigDecimal().setScale(decimalPlaces, RoundingMode.HALF_UP).toFloat()
    }

    fun celsiusToFahrenheit(celsius: Float?): Float? {
        // Calculation from C -> F
        // Source https://en.wikipedia.org/wiki/Fahrenheit
        if (celsius == null) {
            return null
        }
        return (celsius * 1.8F) + 32
    }

    fun fahrenheitToCelsius(fahrenheit: Float?): Float? {
        if (fahrenheit == null) {
            return null
        }
        return (fahrenheit - 32) / 1.8F
    }

    fun msToKmH(ms: Float?): Float? {
        // Calculation from m/s to km/h
        // Source https://en.wikipedia.org/wiki/Kilometres_per_hour
        if (ms == null) {
            return null
        }
        return ms * 3.6F
    }

    fun kmHToMs(kmH: Float?): Float? {
        if (kmH == null) {
            return null
        }
        return kmH / 3.6F
    }


    object TAGS {
        const val WEATHER_DATA_TAG = "WEATHER_DATA"
        const val CITIES_DATA_TAG = "CITIES_DATA"
        const val SETTINGS_DATA_TAG = "SETTINGS_DATA"
    }
}