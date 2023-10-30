package com.example.vejrapp.data

data class preferenceUiState(
    val tempUnit: String,
    val windUnit: String,
    val pressureUnit: String,
    val favoriteCities: List<String> = listOf(),
)

