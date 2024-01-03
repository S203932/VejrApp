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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    locations: Locations,
    val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_TEXT)
    val searchText = _searchText.asStateFlow()

    private val _currentCity = MutableStateFlow(DefaultData.LOCATIONS.CITY)
    val currentCity = _currentCity.asStateFlow()

    private val _searchMode = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_MODE)
    val searchMode = _searchMode.asStateFlow()

    private val _cities = MutableStateFlow(locations.cities)
    val cities = searchText
        .debounce(100L)
        .combine(_cities) { text, cities ->
            if (text.isBlank()) {
                cities
            } else {
                cities.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _cities.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun updateFavorite(city: City) {
        city.favorite = !city.favorite
    }

    fun updateCurrentCity(city: City) {
        _currentCity.value = city
        // TODO Check if correct
        weatherRepository.city = city
        weatherRepository.updateComplete()
    }

    fun updateSearchMode(searchMode: Boolean) {
        _searchMode.value = searchMode
    }
}
