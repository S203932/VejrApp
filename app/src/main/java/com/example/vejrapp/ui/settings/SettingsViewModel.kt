package com.example.vejrapp.ui.settings

//import androidx.core.content.ContextCompat.getString
import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import com.example.vejrapp.R
import com.example.vejrapp.ui.settings.models.SettingsModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    val gson = Gson()
    private val _temperatureUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.settings_temperature_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.settings_temperature_choice_celsius)),
                Pair(true, getString(context, R.string.settings_temperature_choice_fahrenheit))
            )
        )
    )
    val temperatureUnit = _temperatureUnit.asStateFlow()

    private val _windSpeedUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.settings_wind_speed_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.settings_wind_speed_choice_ms)),
                Pair(true, getString(context, R.string.settings_wind_speed_choice_kmh))
            )
        )
    )
    val windSpeedUnit = _windSpeedUnit.asStateFlow()

    private val _pressureUnit = MutableStateFlow(
        SettingsModel(
            name = getString(context, R.string.settings_pressure_name),
            choices = mapOf(
                Pair(false, getString(context, R.string.settings_pressure_choice_bar)),
                Pair(true, getString(context, R.string.settings_pressure_choice_pa))
            )
        )
    )
    val pressureUnit = _pressureUnit.asStateFlow()

    fun toggleTemperatureUnit() {
        _temperatureUnit.value =
            _temperatureUnit.value.copy(checked = !_temperatureUnit.value.checked)
    }

    fun toggleWindSpeedUnit() {
        _windSpeedUnit.value = _windSpeedUnit.value.copy(checked = !_windSpeedUnit.value.checked)
    }

    fun togglePressureUnit() {
        _pressureUnit.value = _pressureUnit.value.copy(checked = !_pressureUnit.value.checked)
    }
}