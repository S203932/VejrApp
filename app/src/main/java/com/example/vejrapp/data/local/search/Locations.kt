package com.example.vejrapp.data.local.search

import android.content.Context
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.search.models.City
import com.google.gson.Gson
import java.io.BufferedReader

class Locations(private val context: Context) {

    private val citiesAssetPath = "filtered_dataset_100000_tz.json"
    private val gson = Gson()

    val cities = gson.fromJson(
        context.assets.open(citiesAssetPath).bufferedReader().use(BufferedReader::readText),
        Array<City>::class.java
    ).toList()

    // TODO Get favorite city from data store
    val selectedCity = DefaultData.LOCATIONS.CITY

//    fun getCityFromLocation(): City {
//
//    }

}