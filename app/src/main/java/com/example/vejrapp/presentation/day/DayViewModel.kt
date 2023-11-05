package com.example.vejrapp.presentation.day


import androidx.lifecycle.ViewModel
import com.example.vejrapp.data.remote.locationforecast.Locationforecast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(locationforecast: Locationforecast) : ViewModel() {

}