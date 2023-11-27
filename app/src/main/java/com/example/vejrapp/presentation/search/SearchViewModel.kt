package com.example.vejrapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.local.search.Locations
import com.example.vejrapp.data.local.search.models.City
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

interface ISearchViewModel {
    val searchText: StateFlow<String>
    val currentCity: StateFlow<City>
    val searchMode: StateFlow<Boolean>
    val cities: StateFlow<List<City>>

    fun onSearchTextChange(text: String)
    fun updateFavorite(city: City)
    fun updateCurrentCity(city: City)
    fun updateSearchMode(searchMode: Boolean)
}

class SearchViewModelPreview @Inject constructor() :
    ISearchViewModel {
    override val searchText: StateFlow<String>
        get() {
            return MutableStateFlow<String>(DefaultData.LOCATIONS.SEARCH_TEXT)
        }

    override val currentCity: StateFlow<City> = MutableStateFlow<City>(DefaultData.LOCATIONS.CITY)
    override val searchMode: StateFlow<Boolean> =
        MutableStateFlow<Boolean>(DefaultData.LOCATIONS.SEARCH_MODE)
    override val cities = MutableStateFlow<List<City>>(DefaultData.LOCATIONS.CITIES)

    override fun onSearchTextChange(text: String) {
        TODO("Not yet implemented")
    }

    override fun updateFavorite(city: City) {
        TODO("Not yet implemented")
    }

    override fun updateCurrentCity(city: City) {
        TODO("Not yet implemented")
    }

    override fun updateSearchMode(searchMode: Boolean) {
        TODO("Not yet implemented")
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    locations: Locations,
    val weatherRepository: WeatherRepository
) : ViewModel(), ISearchViewModel {
    private val _searchText = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_TEXT)
    override val searchText = _searchText.asStateFlow()

    private val _currentCity = MutableStateFlow(DefaultData.LOCATIONS.CITY)
    override val currentCity = _currentCity.asStateFlow()

    private val _searchMode = MutableStateFlow(DefaultData.LOCATIONS.SEARCH_MODE)
    override val searchMode = _searchMode.asStateFlow()

    private val _cities = MutableStateFlow(locations.cities)
    override val cities = searchText
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

    override fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    override fun updateFavorite(city: City) {
        city.favorite = !city.favorite
    }

    override fun updateCurrentCity(city: City) {
        _currentCity.value = city
        // TODO Check if correct
        weatherRepository.city = city
        weatherRepository.updateComplete()
    }

    override fun updateSearchMode(searchMode: Boolean) {
        _searchMode.value = searchMode
    }
}
