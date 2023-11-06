package com.example.vejrapp.presentation.day


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.local.search.Locations
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import com.example.vejrapp.data.remote.locationforecast.LocationforecastImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    locationforecast: Locationforecast, locations: Locations
) : ViewModel() {

    private val locationforecast = locationforecast

    private val _complete = MutableStateFlow(LocationforecastImplementation.defaultComplete)
    val complete = _complete.asStateFlow()

    private val _city = MutableStateFlow(locations.defaultCity)
    val city = _city.asStateFlow()

    // Get forecast for the default city when starting the app
    init {
        updateComplete()
    }

    fun updateComplete() {
        viewModelScope.launch {
            _complete.value = locationforecast.getComplete(
                latitude = city.value.latitude,
                longitude = city.value.longitude
            )
            Log.d("API call", complete.value.toString())
        }
    }
}