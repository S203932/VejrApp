package com.example.vejrapp

import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.preferenceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataViewModel : ViewModel() {
    private val _tempUnit = MutableStateFlow("Celsius")
    val tempUnit = _tempUnit.asStateFlow()

    private val _windUnit = MutableStateFlow("m/s")
    val windUnit = _windUnit.asStateFlow()

    private val _pressureUnit = MutableStateFlow("Bar")
    val pressureUnit = _pressureUnit.asStateFlow()

    private val _uiState =
        MutableStateFlow(
            preferenceUiState(
                favoriteCities = listOf(),
                tempUnit = "",
                windUnit = "",
                pressureUnit = ""
            )
        )
    val uiState: StateFlow<preferenceUiState> = _uiState.asStateFlow()


    fun updateTempUnit(temp: String) {
        _tempUnit.value = temp
    }

    fun updateWindUnit(wind: String) {
        _windUnit.value = wind
    }

    fun updatePressureUnit(pressure: String) {
        _pressureUnit.value = pressure
    }

    //Set desired tempUnit
    fun setTempUnit(temp: String) {
        _uiState.update { currentState ->
            currentState.copy(tempUnit = temp)
        }
    }

    fun getTempUnit(): String {
        return _uiState.value.tempUnit
    }


    //Set desired windUnit
    fun SetWindUnit(wind: String) {
        _uiState.update { currentState ->
            currentState.copy(windUnit = wind)
        }
    }

    //Set desired pressureUnit
    fun setPressureUnit(pressure: String) {
        _uiState.update { currentState ->
            currentState.copy(pressureUnit = pressure)
        }
    }


}

