package com.example.vejrapp.ui.weatherScreens


import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// the actual dayviewmodel that contains
// the currentWeather object generated from the information
// in the api call
@HiltViewModel
class WeatherScreenViewModel @Inject constructor(weatherRepository: WeatherRepository) :
    ViewModel() {
    val weatherData = weatherRepository.weatherData.asStateFlow()
}