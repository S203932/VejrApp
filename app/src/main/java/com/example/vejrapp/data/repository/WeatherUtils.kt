package com.example.vejrapp.data.repository

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
}