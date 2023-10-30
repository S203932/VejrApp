package com.example.vejrapp.search.models

data class City(
    val country: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val population: Int,
    var favorite: Boolean = false,
    var currentLocation: Boolean = false
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true) || country.contains(query, ignoreCase = true)
    }
}
