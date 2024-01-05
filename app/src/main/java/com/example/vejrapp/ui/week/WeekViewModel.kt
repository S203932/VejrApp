package com.example.vejrapp.ui.week

import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeekViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    val weekWeather = weatherRepository.weekWeather
}