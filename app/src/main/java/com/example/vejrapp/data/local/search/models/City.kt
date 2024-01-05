package com.example.vejrapp.data.local.search.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vejrapp.R

// Class to describe each individual city within the json database
// The information about the city is:
// - the country it's in
// - the latitude and longitiude for the city in question
// - the name
// - the population of the city (not necessary, but can be a usable fact feature later)
// . the timezone of the city
// - the favorite status of the city (whether the city is favorited or not)
data class City(
    val country: String,
    val latitude: Float,
    val longitude: Float,
    val name: String,
    val population: Int,
    val timezone: String,
    var favorite: Boolean = false,
) {
    // function to be used when searching
    // checks if the string passed to the function matches part of the city name or country
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true) || country.contains(query, ignoreCase = true)
    }

    // function to check to return the favorite icon relevant to the current city favorite status
    fun favoriteIcon(): ImageVector {
        return if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder
    }

    // Function to return the id value of the favorite status of the city
    fun favoriteDescriptionId(): Int {
        return if (favorite) R.string.favorite_city else R.string.unfavorite_city
    }

    fun uniqueId(): String {
        return "$latitude/$longitude"
    }
}