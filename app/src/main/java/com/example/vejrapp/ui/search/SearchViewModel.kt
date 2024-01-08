package com.example.vejrapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.search.Locations
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val locations: Locations,
    val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_TEXT)
    val searchText = _searchText.asStateFlow()

    private val _currentCity = MutableStateFlow(DefaultData.LOCATIONS.CITY)
    val currentCity = _currentCity.asStateFlow()

    private val _favoriteCities = MutableStateFlow(DefaultData.LOCATIONS.FAVORITE_CITIES)
    private var favoriteCities = _favoriteCities.asStateFlow()

    private val _searchMode = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_MODE)
    val searchMode = _searchMode.asStateFlow()

    private val _cities = MutableStateFlow<List<City>>(listOf())
    val cities = searchText
        .debounce(100L)
        .combine(_cities) { text, cities ->
            val updatedCities = cities.map {
                if (it.uniqueId() == currentCity.value.uniqueId()) currentCity.value else it
            }
            val sortedCities = if (text.isBlank()) {
                updatedCities.sortedWith(compareByDescending<City>() { it.favorite }.thenByDescending { it.population })
            } else {
                updatedCities.filter { it.doesMatchSearchQuery(text) }
                    .sortedWith(compareByDescending<City>() { it.favorite }.thenByDescending { it.population })
            }
            sortedCities
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _cities.value
        )

    init {
        viewModelScope.launch {
            locations.cities.collect { cities ->
                _cities.value = cities
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun updateFavorite(city: City) {
        val updatedCity = city.copy(favorite = !city.favorite)

        // Update favorite cities
        _favoriteCities.value = _favoriteCities.value.map {
            if (it.uniqueId() == city.uniqueId()) updatedCity else it
        }

        // Update current city
        if (city.uniqueId() == _currentCity.value.uniqueId()) {
            _currentCity.value = updatedCity
        }

        // Update the entire city list
        _cities.value = _cities.value.map {
            if (it.uniqueId() == city.uniqueId()) updatedCity else it
        }

        locations.saveCities(_cities.value)
    }


    fun getFavorite() {
        //  locations.getFavoriteCities()
        locations.favoriteCities.value = _cities.value
    }

    fun updateCurrentCity(city: City) {
        _currentCity.value = city
        weatherRepository.city = city
        weatherRepository.getComplete()
    }

    fun updateSearchMode(searchMode: Boolean) {
        _searchMode.value = searchMode
    }
}
