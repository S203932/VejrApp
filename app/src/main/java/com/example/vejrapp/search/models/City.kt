package com.example.vejrapp.search.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vejrapp.R

data class City(
    val country: String,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val population: Int,
    var favorite: Boolean = false,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true) || country.contains(query, ignoreCase = true)
    }

    fun favoriteIcon(): ImageVector {
        return if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder
    }

    fun favoriteDescriptionId(): Int {
        return if (favorite) R.string.favorite_city else R.string.unfavorite_city
    }
}
