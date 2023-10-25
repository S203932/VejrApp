package com.example.vejrapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _currentCity = MutableStateFlow("")
    val currentCity = _currentCity.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _cities = MutableStateFlow(allCities)
    val cities = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_cities) { text, cities ->
            if (text.isBlank()) {
                cities
            } else {
                cities.filter {
                    it.doesMatchSearchQuery(text)
                }
            }


        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _cities.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun favoriteUpdate(city: City) {
        city.favorite = !city.favorite
    }

    fun updateCurrentCity(cityName: String) {
        _currentCity.value = cityName
    }

}


//The data below should probably be in the domain layer, but we don't
//have that project structure yet, so for now it's here

data class City(
    val name: String,
    val coordinatesDD1: Double,
    val coordinatesDD2: Double,
    var favorite: Boolean,
) {


    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "$coordinatesDD1,$coordinatesDD2",

            )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }


}

//Predefined list of cities, api will replace the data
private val allCities = listOf(
    City(
        name = "Copenhagen",
        coordinatesDD1 = 55.67594,
        coordinatesDD2 = 12.56553,
        favorite = false
    ),
    City(
        name = "Odense", 55.39594, 10.38831, false
    ),
    City(
        name = "Frederiksberg", 55.67938, 12.53463, false
    ),
    City(
        name = "Naestved", 55.22992, 11.76092, false
    )
)