package com.example.vejrapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.locations.models.City
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
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_TEXT)
    val searchText = _searchText.asStateFlow()

    val currentCity = weatherRepository.primaryCity.asStateFlow()

    private val _searchMode = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_MODE)
    val searchMode = _searchMode.asStateFlow()

    private val _displayedCities = weatherRepository.cities.asStateFlow()
    val displayedCities = searchText
        .debounce(100L)
        .combine(_displayedCities) { text, cities ->
            val updatedCities = cities?.map {
                if (it.uniqueId() == currentCity.value?.uniqueId()) currentCity.value else it
            }
            val sortedCities = if (text.isBlank()) {
                updatedCities?.sortedWith(compareByDescending<City?> { it!!.favorite }.thenByDescending { it!!.population })
            } else {
                updatedCities?.filter { it!!.doesMatchSearchQuery(text) }
                    ?.sortedWith(compareByDescending<City?> { it!!.favorite }.thenByDescending { it!!.population })
            }
            sortedCities

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _displayedCities.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun updateFavorite(city: City) {
        val updatedCity = city.copy(favorite = !city.favorite)

        if (city.uniqueId() == currentCity.value?.uniqueId()) {
            updateCurrentCity(updatedCity)
//            _currentCity.value = updatedCity
        }
        // Update the entire city list
        weatherRepository.updateCities(weatherRepository.cities.value!!.map {
            if (it.uniqueId() == city.uniqueId()) updatedCity else it
        })
    }

    fun updateCurrentCity(city: City) {
        // Set as the selected city for the next session if favorite
        weatherRepository.updatePrimaryCity(city)
        
        viewModelScope.launch {
            weatherRepository.getWeatherData()
        }
    }


    fun updateSearchMode(searchMode: Boolean) {
        _searchMode.value = searchMode
    }
}
