package com.example.vejrapp.presentation.settings

import android.content.Context
import androidx.core.content.ContextCompat.getString
//import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import com.example.vejrapp.R
import com.example.vejrapp.presentation.settings.models.SettingsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface ISettingsViewModel {
    val temperatureUnit: StateFlow<SettingsModel>
    val windSpeedUnit: StateFlow<SettingsModel>
    val pressureUnit: StateFlow<SettingsModel>

    fun toggleTemperatureUnit()
    fun toggleWindSpeedUnit()
    fun togglePressureUnit()
}

class SettingsViewModelPreview @Inject constructor() :
    ISettingsViewModel {
    override val temperatureUnit: StateFlow<SettingsModel>
        get() = TODO("Not yet implemented")
    override val windSpeedUnit: StateFlow<SettingsModel>
        get() = TODO("Not yet implemented")
    override val pressureUnit: StateFlow<SettingsModel>
        get() = TODO("Not yet implemented")

    override fun toggleTemperatureUnit() {
        TODO("Not yet implemented")
    }

    override fun toggleWindSpeedUnit() {
        TODO("Not yet implemented")
    }

    override fun togglePressureUnit() {
        TODO("Not yet implemented")
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel(), ISettingsViewModel {
    private val _temperatureUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.temperature_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.temperature_choice_celsius)),
                Pair(true, getString(context, R.string.temperature_choice_fahrenheit))
            )
        )
    )
    override val temperatureUnit = _temperatureUnit.asStateFlow()

    private val _windSpeedUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.wind_speed_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.wind_speed_choice_ms)),
                Pair(true, getString(context, R.string.wind_speed_choice_kmh))
            )
        )
    )
    override val windSpeedUnit = _windSpeedUnit.asStateFlow()

    private val _pressureUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.pressure_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.pressure_choice_bar)),
                Pair(true, getString(context, R.string.pressure_choice_pa))
            )
        )
    )
    override val pressureUnit = _pressureUnit.asStateFlow()

    override fun toggleTemperatureUnit() {
        _temperatureUnit.value =
            _temperatureUnit.value.copy(checked = !_temperatureUnit.value.checked)
    }

    override fun toggleWindSpeedUnit() {
        _windSpeedUnit.value = _windSpeedUnit.value.copy(checked = !_windSpeedUnit.value.checked)
    }

    override fun togglePressureUnit() {
        _pressureUnit.value = _pressureUnit.value.copy(checked = !_pressureUnit.value.checked)
    }
}

