package com.example.vejrapp.presentation.week

import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.repository.WeatherRepository
import com.example.vejrapp.data.repository.models.WeekWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface IWeekViewModel {
    val weekWeather: StateFlow<WeekWeather>
    fun update()
}

class WeekViewModelPreview() : IWeekViewModel {
    override val weekWeather =
        MutableStateFlow<WeekWeather>(DefaultData.LOCATIONFORECAST.WEEK_WEATHER)

    override fun update() {
        TODO("Not yet implemented")
    }
}

@HiltViewModel
class WeekViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel(), IWeekViewModel {
    override val weekWeather = weatherRepository.weekWeather

    override fun update() {
        weatherRepository.updateComplete()
    }
}