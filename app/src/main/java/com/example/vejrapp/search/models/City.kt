package com.example.vejrapp.search.models

data class City(
    val country: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val population: Int,
    val favourite: Boolean = false,
    val currentLocation: Boolean = false
)
