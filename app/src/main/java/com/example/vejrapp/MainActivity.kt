package com.example.vejrapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vejrapp.locationforecast.LocationforecastApi
import com.example.vejrapp.locationforecast.RetroFitHelper
import com.example.vejrapp.locationforecast.models.METJSONForecast
import com.example.vejrapp.search.Locations
import com.example.vejrapp.search.models.City
import com.example.vejrapp.ui.theme.VejrAppTheme
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {

    val gson = Gson()
    val scope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {

//        val mETJSONForecast: METJSONForecast = gson.fromJson(
//            applicationContext.assets.open("yr_complete_example.json").bufferedReader()
//                .use(BufferedReader::readText),
//            METJSONForecast::class.java
//        )
//        Log.d("METJSONForecast", mETJSONForecast.toString())

        super.onCreate(savedInstanceState)
        setContent {
            VejrAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        Greeting()
                        SearchBar(modifier = Modifier)

                    }

                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(modifier: Modifier = Modifier) {

        val locations = Locations(context = application.baseContext)
        var value by remember { mutableStateOf("") }
        var searchResults by remember { mutableStateOf(locations.cities) }

        Column(modifier = modifier) {
            Box(modifier = modifier.fillMaxWidth()) {
                TextField(
                    value = value,
                    onValueChange = {
                        value = it
                        searchResults = if (it.isNotBlank()) {
                            locations.searchCity(it)
                        } else {
                            locations.cities
                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    label = { Text(text = getString(R.string.search_label)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },

                    modifier = modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(25))

                )
                IconButton(
                    onClick = {},
                    modifier = modifier
                        .align(alignment = Alignment.CenterEnd)
                        .align(alignment = Alignment.TopEnd)
                        .size(35.dp)
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            }


            LazyColumn {

                item {
                    // TODO place current location here
                }

                items(items = searchResults) { searchResult ->
                    CitySearchResult(modifier, searchResult)
                }
            }


        }


    }

    @Composable
    fun CitySearchResult(modifier: Modifier = Modifier, city: City) {
        Row(modifier = modifier) {
            Text(
                text = "${city.name} - ${city.country}",
            )
            IconToggleButton(
                checked = city.favourite,
                onCheckedChange = { },
                content = {
                    if (city.favourite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                },
                modifier = modifier
            )
        }
    }
}


@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val scope = CoroutineScope(Dispatchers.IO)
//    val locationforecastApi = RetroFitHelper.getInstance().create(LocationforecastApi::class.java)
    Column {
        Button(modifier =
        modifier.fillMaxWidth(),
            onClick = {
                scope.launch { testAPI() }
//                testAPI()

//                scope.launch {
//
//                    try {
////                    val locationforecastApi =
////                        RetroFitHelper.getInstance().create(LocationforecastApi::class.java)
//                        val response =
//                            locationforecastApi.getComplete(
//                                latitude = 12.4932F,
//                                longitude = 55.7995F
//                            ).awaitResponse()
//
//                        if (response.isSuccessful) {
//                            Log.d("Success", response.body().toString())
//                            Log.d("Success", response.message().toString())
//                            Log.d("Success", response.raw().body?.contentLength().toString())
//                            Log.d("Success", response.raw().body?.contentType().toString())
//                            Log.d("Success", response.raw().body?.contentLength().toString())
//                            Log.d("Success", response.raw().message)
//                        }
//                    } catch (e: Exception) {
//                        Log.d("Error", e.message.toString())
//                        Log.d("Error", e.cause.toString())
//                        Log.d("Error", e.toString())
//                        Log.d("Error", e.printStackTrace().toString())
//                    }
//                }
            }
        ) {}
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VejrAppTheme {
        Greeting()
    }
}

fun testAPI() {
    val locationforecastApi = RetroFitHelper.getInstance().create(LocationforecastApi::class.java)
    val call = locationforecastApi.getComplete(
        lat = 55.7956F,
        lon = 12.4718F
    )
    Log.d("CALL", call.request().headers.toString())

    call.enqueue(object : Callback<METJSONForecast> {
        override fun onResponse(call: Call<METJSONForecast>, response: Response<METJSONForecast>) {
            Log.d("API_CALL", response.code().toString())
            Log.d("API_CALL", response.headers().toString())

            Log.d("API_CALL", response.body().toString())
            Log.d("API_CALL", response.message().toString())
            Log.d("API_CALL", response.raw().body?.contentLength().toString())
            Log.d("API_CALL", response.raw().body?.contentType().toString())
            Log.d("API_CALL", response.raw().body?.contentLength().toString())
            Log.d("API_CALL", response.raw().message)
        }

        override fun onFailure(call: Call<METJSONForecast>, t: Throwable) {
            Log.d("FAILURE", call.toString())
            Log.d("FAILURE", t.toString())
            Log.d("FAILURE", t.message.toString())
        }

    })
//    try {
//        val response =
//            locationforecastApi.getComplete(
//                latitude = 12.4932F,
//                longitude = 55.7995F
//            ).awaitResponse()
//
//        if (response.isSuccessful) {
//            Log.d("Success", response.body().toString())
//            Log.d("Success", response.message().toString())
//            Log.d("Success", response.raw().body?.contentLength().toString())
//            Log.d("Success", response.raw().body?.contentType().toString())
//            Log.d("Success", response.raw().body?.contentLength().toString())
//            Log.d("Success", response.raw().message)
//        }
//    } catch (e: Exception) {
//        Log.d("Error", e.message.toString())
//        Log.d("Error", e.cause.toString())
//        Log.d("Error", e.toString())
//        Log.d("Error", e.printStackTrace().toString())
//    }
}