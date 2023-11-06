package com.example.vejrapp.data.remote.locationforecast

import com.example.vejrapp.data.remote.locationforecast.models.Forecast
import com.example.vejrapp.data.remote.locationforecast.models.ForecastMeta
import com.example.vejrapp.data.remote.locationforecast.models.ForecastUnits
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecast
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastEnum
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometry
import com.example.vejrapp.data.remote.locationforecast.models.PointGeometryEnum
import com.example.vejrapp.data.remote.locationforecast.models.Status
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.ZonedDateTime

class ZonedDateTimeDeserializer(val dateTime: String) : JsonDeserializer<ZonedDateTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZonedDateTime {

        // Default to the dawn of time if date can't be deserialized
        return if (json != null) {
            ZonedDateTime.parse(json.asString)
        } else {
            ZonedDateTime.parse("1970-01-01T00:00:00Z")
        }
    }

}

class LocationforecastImplementation : Locationforecast {

    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/"

    // Add custom intercepter to add header by default
    private val customHttpClient = OkHttpClient().newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    name = "User-Agent",
                    value = "VejrApp https://github.com/S203932/VejrApp"
                ).build()
            chain.proceed(request)
        }

    // Add custom deserializer for ZonedDateTime
    private val gson =
        GsonBuilder().registerTypeAdapter(
            ZonedDateTime::class.java,
            ZonedDateTimeDeserializer("1970-01-01T00:00:00Z")
        ).create()

    // Create retrofit object with custom deserializer and interceptor
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(customHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiClient: Locationforecast = retrofit.create(Locationforecast::class.java)

    override suspend fun getComplete(
        altitude: Int?,
        latitude: Float,
        longitude: Float
    ): METJSONForecast {
        return apiClient.getComplete(altitude, latitude, longitude)
    }

    override suspend fun getStatus(): Status {
        return apiClient.getStatus()
    }

    // object for getting a default value for a complete forecast
    companion object {
        val defaultComplete = METJSONForecast(
            geometry = PointGeometry(
                coordinates = listOf(0F, 0F, 0F),
                type = PointGeometryEnum.Point
            ),
            properties = Forecast(
                meta = ForecastMeta(units = ForecastUnits(), updatedAt = ZonedDateTime.now()),
                timeseries = listOf()
            ),
            type = METJSONForecastEnum.Feature
        )
    }
}