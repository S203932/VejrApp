package com.example.vejrapp.data.remote.locationforecast

import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.Status
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.ZonedDateTime

private val gson =
    GsonBuilder().registerTypeAdapter(
        ZonedDateTime::class.java,
        ZonedDateTimeDeserializer()
    ).create()

// Deserializer for ZonedDateTime
class ZonedDateTimeDeserializer() : JsonDeserializer<ZonedDateTime> {
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

// Set the User-Agent header for request and add expires and last_modified to response from headers
class CustomInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return response.newBuilder()
            .addHeader(
                name = "User-Agent",
                value = "VejrApp https://github.com/S203932/VejrApp"
            )
            .body(addTimeStampFromHeader(response))
            .build()
    }

    private fun addTimeStampFromHeader(response: Response): ResponseBody {
        return gson.toJson(
            hashMapOf(
                "met_json_forecast" to JsonParser().parse(response.body?.string()),
                "expires" to response.header("expires").toString(),
                "last_modified" to response.header("last-modified").toString()
            )
        ).toString().toResponseBody(response.body?.contentType())
    }
}

class LocationforecastImplementation : Locationforecast {

    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/"

    private val customHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(CustomInterceptor())

    // Create retrofit object with custom deserializer and okhttp interceptor
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
    ): METJSONForecastTimestamped {
        return apiClient.getComplete(altitude, latitude, longitude)
    }

    override suspend fun getStatus(): Status {
        return apiClient.getStatus()
    }
}
