package com.example.vejrapp.UI.day


import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.local.default.DefaultData
import com.example.vejrapp.data.repository.WeatherRepository
import com.example.vejrapp.data.repository.models.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Interface to determine the things needed in dayviewmodel
interface IDayViewModel {
    val currentWeather: StateFlow<CurrentWeather>
    fun update()
}

// preview of the dayviewmodel
class DayViewModelPreview() : IDayViewModel {
    override val currentWeather =
        MutableStateFlow<CurrentWeather>(DefaultData.LOCATIONFORECAST.CURRENT_WEATHER)

    override fun update() {
        TODO("Not yet implemented")
    }
}

// the actual dayviewmodel that contains
// the currentWeather object generated from the information
// in the api call
@HiltViewModel
class DayViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel(), IDayViewModel {
    override val currentWeather = weatherRepository.currentWeather

    override fun update() {
        weatherRepository.updateComplete()
    }
}