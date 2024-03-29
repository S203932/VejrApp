package com.example.vejrapp.data.local.locations

import android.content.Context
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.locations.models.City
import com.example.vejrapp.data.repository.WeatherUtils.gson
import java.io.BufferedReader
import javax.inject.Inject

class Locations @Inject constructor(
    private val context: Context
) {
    private val citiesAssetPath = "filtered_dataset_100000_tz.json"

    var cities = gson.fromJson(
        context.assets.open(citiesAssetPath).bufferedReader()
            .use(BufferedReader::readText),
        Array<City>::class.java
    ).toList()

    var primaryCity: City = DefaultData.LOCATIONS.CITY
}

