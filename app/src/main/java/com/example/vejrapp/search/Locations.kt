package com.example.vejrapp.search

import android.content.Context
import com.example.vejrapp.search.models.City
import com.google.gson.Gson
import java.io.BufferedReader

class Locations(private val context: Context) {

    private val citiesAssetPath = "filtered_dataset_100000.json"
    private val gson = Gson()

    val cities = gson.fromJson(
        context.assets.open(citiesAssetPath).bufferedReader().use(BufferedReader::readText),
        Array<City>::class.java
    ).toList()

    // Set the default city to the first occurrence of Copenhagen
    val defaultCity = cities.first { it.name == "Copenhagen" }

}