package com.example.vejrapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.search.Locations
import com.example.vejrapp.search.models.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(locations: Locations) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _currentCity = MutableStateFlow(locations.defaultCity)
    val currentCity = _currentCity.asStateFlow()

    private val _searchMode = MutableStateFlow(false)
    val searchMode = _searchMode.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _cities = MutableStateFlow(locations.cities)

    @OptIn(FlowPreview::class)
    val cities = searchText
        .debounce(100L)
        .onEach { _isSearching.update { true } }
        .combine(_cities) { text, cities ->
            if (text.isBlank()) {
                cities
            } else {
                cities.filter {
                    it.name.contains(text, ignoreCase = true)
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

    fun updateCurrentCity(city: City) {
        _currentCity.value = city
    }

    fun updateSearchMode(searchMode: Boolean) {
        _searchMode.value = searchMode
    }
}
