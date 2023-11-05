package com.example.vejrapp.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor() : ViewModel() {
    private val _tempUnit = MutableStateFlow("Celsius")
    val tempUnit = _tempUnit.asStateFlow()

    private val _windUnit = MutableStateFlow("m/s")
    val windUnit = _windUnit.asStateFlow()

    private val _pressureUnit = MutableStateFlow("Bar")
    val pressureUnit = _pressureUnit.asStateFlow()

    fun updateTempUnit(temp: String) {
        _tempUnit.value = temp
    }

    fun updateWindUnit(wind: String) {
        _windUnit.value = wind
    }

    fun updatePressureUnit(pressure: String) {
        _pressureUnit.value = pressure
    }
    
}

