package com.example.vejrapp.ui.settings

//import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val settings = weatherRepository.settings

    fun toggleTemperatureUnit() {
        settings.value =
            settings.value.copy(temperatureSetting = settings.value.temperatureSetting.copy(checked = !settings.value.temperatureSetting.checked))
        updateSettings()
    }

    fun toggleWindSpeedUnit() {
        settings.value =
            settings.value.copy(windSpeedSetting = settings.value.windSpeedSetting.copy(checked = !settings.value.windSpeedSetting.checked))
        updateSettings()
    }

    fun togglePressureUnit() {
        settings.value =
            settings.value.copy(pressureSetting = settings.value.pressureSetting.copy(checked = !settings.value.pressureSetting.checked))
        updateSettings()
    }

    private fun updateSettings() {
        viewModelScope.launch {
            weatherRepository.updateSettings()
        }
    }
}