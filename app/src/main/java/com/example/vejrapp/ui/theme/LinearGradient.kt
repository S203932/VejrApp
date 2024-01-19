package com.example.vejrapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.vejrapp.R
import com.example.vejrapp.data.mapToYRImageResource
import com.example.vejrapp.data.repository.WeatherUtils.getCurrentIndex
import com.example.vejrapp.ui.weatherScreens.WeatherScreenViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

// Function to create the gradiant background in the app

@Composable
fun LinearGradient(weatherScreenViewModel: WeatherScreenViewModel) {
    var dayIndex = 0
    val localDateTime = LocalDateTime.now()
    if (localDateTime.truncatedTo(ChronoUnit.DAYS) != LocalDateTime.now()
            .truncatedTo(ChronoUnit.DAYS)
    ) {
        dayIndex = abs(localDateTime.compareTo(LocalDateTime.now()))
    }

    val weatherData by weatherScreenViewModel.weatherData.collectAsState()
    // Ensure gradients are only found if data is available
    if (weatherData == null) {
        return
    }
    val indexOfHour = getCurrentIndex(weatherData!!, dayIndex)

    val dataCurrentHour = weatherData!!.data.days[dayIndex].hours[indexOfHour].data
    val weatherImage = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val imageRes = weatherImage.mapToYRImageResource()

    // RGB values for #525252 and #333333
    // Nighttime theme:
    val NighttimestartColor = Color(0xFFB4B6BE)
    val NighttimeendColor = Color(0xFF91939A)

    //Evening theme:
    val EveningstartColor = Color(0xFFFCCA5C)
    val EveningendColor = Color(0xFF40364E)

    //Daytime theme:
    val DaystartColor = Color(0xFF43A6DB)
    val DayendColor = Color(0xFF2487BC)

    //Cloudy theme:
    val CloudystartColor = Color(0xFF43A6DB)
    val CloudyendColor = Color(0xFF91939A)

    /*
    var gradient =
        // Default to nighttime theme if the weather condition is not matched
        Brush.linearGradient(
            00.0f to Color.Magenta,
            50.0f to Color.Cyan,
            start = Offset(0f, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )

    if (weatherImage.contains("day")) {
        if (weatherImage.contains("clear")) {
            gradient = Brush.linearGradient(
                0.0f to DaystartColor,
                1.0f to DayendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }
    } else if (weatherImage.contains("rain")) {
        if (weatherImage.contains("snow")) {
            gradient = Brush.linearGradient(
                0.0f to CloudystartColor,
                1.0f to CloudyendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }
    } else {
        // Default to nighttime theme if the weather condition is not matched
        gradient = Brush.linearGradient(
            0.0f to DaystartColor,
            1.0f to DayendColor,
            start = Offset(0f, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY)
        )
    }

    Box(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize()
    )
}*/
    val gradient = when (imageRes) {
        R.drawable.clearsky_day, R.drawable.fair_day /*"clearsky_night", "clearsky_polartwilight", "fair_day", "fair_night", "fair_polartwilight"*/
        -> {
            Brush.linearGradient(
                0.0f to DaystartColor,
                1.0f to DayendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        R.drawable.partlycloudy_day, R.drawable.lightsnowshowers_day, R.drawable.cloudy, R.drawable.lightrainshowersandthunder_day,
        R.drawable.lightrainshowersandthunder_polartwilight, R.drawable.lightrainshowers_day, R.drawable.lightsnowshowers_polartwilight,
        R.drawable.heavyrainandthunder, R.drawable.heavysnowandthunder, R.drawable.rainandthunder, R.drawable.heavysleetshowers_day,
        R.drawable.heavysnow, R.drawable.heavyrainshowers_day, R.drawable.lightsleet, R.drawable.heavyrain, R.drawable.lightrainshowers_day,
        R.drawable.heavysleetshowers_day, R.drawable.lightsleetshowers_day, R.drawable.snow, R.drawable.heavyrainshowersandthunder_day,
        R.drawable.snowshowers_day, R.drawable.snowshowersandthunder_day, R.drawable.fog, R.drawable.lightsnowandthunder, R.drawable.heavysleetandthunder,
        R.drawable.lightrain, R.drawable.rainshowersandthunder_day, R.drawable.rain, R.drawable.lightsnow, R.drawable.lightsnow, R.drawable.heavysleet,
        R.drawable.sleetandthunder, R.drawable.lightrainandthunder, R.drawable.sleet, R.drawable.lightssleetshowersandthunder_day, R.drawable.lightsleetandthunder,
        R.drawable.partlycloudy_day, R.drawable.sleetshowersandthunder_day, R.drawable.rainshowers_day, R.drawable.snowandthunder, R.drawable.sleetshowers_day,
        R.drawable.cloudy, R.drawable.heavysnowshowersandthunder_day, R.drawable.heavysnowshowers_day,
        -> {
            Brush.linearGradient(
                0.0f to CloudystartColor,
                1.0f to CloudyendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        R.drawable.clearsky_polartwilight, R.drawable.fair_polartwilight, R.drawable.heavysleetshowersandthunder_polartwilight,
        R.drawable.heavyrainshowers_polartwilight, R.drawable.lightrainshowers_polartwilight, R.drawable.heavysleetshowers_polartwilight,
        R.drawable.lightsleetshowers_polartwilight, R.drawable.heavyrainshowersandthunder_polartwilight, R.drawable.snowshowers_polartwilight,
        R.drawable.snowshowersandthunder_polartwilight, R.drawable.rainshowersandthunder_polartwilight, R.drawable.lightrainshowersandthunder_polartwilight,
        R.drawable.lightssleetshowersandthunder_polartwilight, R.drawable.partlycloudy_polartwilight, R.drawable.sleetshowersandthunder_polartwilight,
        R.drawable.rainshowers_polartwilight, R.drawable.sleetshowers_polartwilight, R.drawable.heavysnowshowersandthunder_polartwilight,
        R.drawable.heavysnowshowers_polartwilight
        -> {
            Brush.linearGradient(
                0.0f to EveningstartColor,
                1.0f to EveningendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        R.drawable.clearsky_night, R.drawable.lightssnowshowersandthunder_night, R.drawable.lightrainshowers_night, R.drawable.heavysleetshowersandthunder_night,
        R.drawable.heavyrainshowers_night, R.drawable.lightrainshowers_night, R.drawable.heavysleetshowers_night, R.drawable.lightsleetshowers_night,
        R.drawable.heavyrainshowersandthunder_night, R.drawable.snowshowers_night, R.drawable.snowshowersandthunder_night, R.drawable.rainshowersandthunder_night,
        R.drawable.lightrainshowersandthunder_night, R.drawable.lightssnowshowersandthunder_night, R.drawable.partlycloudy_night, R.drawable.sleetshowersandthunder_night,
        R.drawable.rainshowers_night, R.drawable.sleetshowers_night, R.drawable.heavysnowshowersandthunder_night, R.drawable.heavysnowshowers_night
        -> {
            Brush.linearGradient(
                0.0f to NighttimestartColor,
                1.0f to NighttimeendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        else -> {
            // Default to nighttime theme if the weather condition is not matched
            Brush.linearGradient(
                00.0f to Color.Magenta,
                50.0f to Color.Cyan,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }
    }

    Box(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize()
    )
}
