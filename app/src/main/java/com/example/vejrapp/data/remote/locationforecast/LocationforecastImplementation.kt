package com.example.vejrapp.data.remote.locationforecast

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.Status
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val gson = GsonBuilder().setLenient().registerTypeAdapter(
    ZonedDateTime::class.java, ZonedDateTimeDeserializer()
).create()

// Deserializer for ZonedDateTime
class ZonedDateTimeDeserializer() : JsonDeserializer<ZonedDateTime> {
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
            ZonedDateTime.parse(json.asString, formatter())
        }
        // Default to the dawn of time if date can't be deserialized
        else {
            ZonedDateTime.parse("1970-01-01T00:00:00Z")
        }
    }
}


class BodyInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val body = JsonObject()
        body.add(
            "met_json_forecast",
            JsonParser().parse(response.body?.string())
        )
        body.addProperty("expires", response.header("expires").toString())
        body.addProperty("last_modified", response.header("last-modified").toString())

        return response.newBuilder()
            .body(gson.toJson(body).toResponseBody(response.body?.contentType())).build()
    }
}

class LocationforecastImplementation : Locationforecast {

    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/"

    private val customHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(BodyInterceptor())

    // Create retrofit object with custom deserializer and okhttp interceptor
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(customHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()

    private val apiClient: Locationforecast = retrofit.create(Locationforecast::class.java)

    override suspend fun getComplete(
        altitude: Int?, latitude: Float, longitude: Float
    ): METJSONForecastTimestamped {
        return apiClient.getComplete(altitude, latitude, longitude)
    }

    override suspend fun getStatus(): Status {
        return apiClient.getStatus()
    }
}
