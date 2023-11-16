package com.example.vejrapp.presentation.day


import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.repository.WeatherRepository
import com.example.vejrapp.data.repository.models.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface IDayViewModel {
    val currentWeather: MutableStateFlow<CurrentWeather>
    fun update()
}

class DayViewModelPreview() : IDayViewModel {
    override val currentWeather =
        MutableStateFlow<CurrentWeather>(DefaultData.defaultCurrentWeather)

    override fun update() {
        TODO("Not yet implemented")
    }
}

@HiltViewModel
class DayViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel(), IDayViewModel {
    override val currentWeather = weatherRepository.currentWeather

    override fun update() {
        weatherRepository.updateComplete()
    }
}