package com.example.vejrapp.data.remote.locationforecast

import android.util.Log
import com.example.vejrapp.data.remote.locationforecast.models.METJSONForecastTimestamped
import com.example.vejrapp.data.remote.locationforecast.models.Status
import com.example.vejrapp.data.repository.WeatherUtils.gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Used for deserialization of the API data
class BodyInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val body = JsonObject()

        // Try to parse body as JSON. Body may be HTML if an error is met
        try {
            body.add(
                "met_json_forecast",
                JsonParser().parse(response.body?.string())
            )
            body.addProperty("expires", response.header("expires").toString())

        } catch (error: Exception) {
            Log.d("API call", "Error encountered in BodyInterceptor $error")
        }

        // The body will be empty if the response body was invalid.
        return response.newBuilder()
            .body(gson.toJson(body).toResponseBody(response.body?.contentType()))
            .build()
    }
}

// The actual fetching of the API data
class LocationforecastImplementation : Locationforecast {
    // The base URL from where we fetch the data
    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/"

    private val customHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(BodyInterceptor())

    // Create retrofit object with custom deserializer and okhttp interceptor
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(customHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()

    private val apiClient = retrofit.create(Locationforecast::class.java)

    override suspend fun getComplete(
        altitude: Int?, latitude: Float, longitude: Float
    ): METJSONForecastTimestamped? {
        return try {
            apiClient.getComplete(altitude, latitude, longitude)
        } catch (error: Exception) {
            null
        }
    }

    override suspend fun getStatus(): Status? {
        return try {
            apiClient.getStatus()
        } catch (error: Exception) {
            null
        }
    }
}