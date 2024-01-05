package com.example.vejrapp.ui.day


import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// the actual dayviewmodel that contains
// the currentWeather object generated from the information
// in the api call
@HiltViewModel
class DayViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    val weatherData = weatherRepository.weatherData

    fun update() {
        weatherRepository.updateComplete()
    }
}