package com.example.vejrapp.ui.settings

//import androidx.core.content.ContextCompat.getString
import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.R
import com.example.vejrapp.data.local.datastore.PreferencesDataStore
import com.example.vejrapp.ui.settings.models.SettingsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStore: PreferencesDataStore
) : ViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            readTemperatureSettings()
            readWindSettings()
            readPressureSettings()
        }
    }

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


    //Reading units settings from PreferencesDataStore
    private fun readTemperatureSettings() {
        viewModelScope.launch {
            dataStore.getTemperaturePreference().collect { preferences ->
                when (preferences) {
                    true -> {
                        _temperatureUnit.value.checked = true
                    }

                    false -> {
                        _temperatureUnit.value.checked = false
                    }
                }

            }
        }
    }

    private fun readWindSettings() {
        viewModelScope.launch {
            dataStore.getWindPreference().collect { preferences ->
                when (preferences) {
                    true -> {
                        _windSpeedUnit.value.checked = true
                    }

                    false -> {
                        _windSpeedUnit.value.checked = false
                    }
                }

            }
        }
    }

    private fun readPressureSettings() {
        viewModelScope.launch {
            dataStore.getPressurePreference().collect { preferences ->
                when (preferences) {
                    true -> {
                        _pressureUnit.value.checked = true
                    }

                    false -> {
                        _pressureUnit.value.checked = false
                    }
                }

            }
        }
    }

    //Changing state of Settings for units (Switch) and saving it in to PreferencesDataStore
    fun toggleTemperatureUnit() {
        val tem = !_temperatureUnit.value.checked
        scope.launch {
            dataStore.updateTemperaturePreference(tem)
        }
        _temperatureUnit.value = _temperatureUnit.value.copy(checked = tem)
    }

    fun toggleWindSpeedUnit() {
        val wind = !_windSpeedUnit.value.checked
        scope.launch {
            dataStore.updateWindPreference(wind)
        }
        _windSpeedUnit.value = _windSpeedUnit.value.copy(checked = wind)
    }

    fun togglePressureUnit() {
        val pressure = !_pressureUnit.value.checked
        scope.launch {
            dataStore.updatePressurePreference(pressure)
        }
        _pressureUnit.value = _pressureUnit.value.copy(checked = pressure)
    }
}


