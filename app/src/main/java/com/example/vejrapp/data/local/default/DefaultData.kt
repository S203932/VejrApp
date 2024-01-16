package com.example.vejrapp.data.local.default

import com.example.vejrapp.data.local.locations.models.City

// The default data used for the repository data, when the api call wasn't
// called yet or was unsuccessfull
class DefaultData {
    object LOCATIONS {
        private val copenhagen = City(
            country = "Denmark",
            latitude = 55.67594F,
            longitude = 12.56553F,
            name = "Copenhagen",
            population = 1153615,
            timezone = "Europe/Copenhagen"
        )
        
        val CITY: City = copenhagen
        val SEARCH_MODE: Boolean = false
        val SEARCH_TEXT: String = ""
    }
}