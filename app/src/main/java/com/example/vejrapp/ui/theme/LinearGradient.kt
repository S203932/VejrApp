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
import com.example.vejrapp.ui.weatherScreens.WeatherScreenViewModel
import com.example.vejrapp.ui.weatherScreens.getCurrentIndex
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
    val indexOfHour = getCurrentIndex(weatherData!!, dayIndex)
    val dataCurrentHour = weatherData!!.data.days[dayIndex].hours[indexOfHour].data
    val weatherImage = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val imageRes = weatherImage.mapToYRImageResource()

    // RGB values for #525252 and #333333
    // Nighttime theme:
    val NighttimestartColor = Color(0xFF525252)
    val NighttimeendColor = Color(0xFF333333)

    //Evening theme:
    val EveningstartColor = Color(0xFF40364E)
    val EveningendColor = Color(0xFF282130)

    //Daytime theme:
    val DaystartColor = Color(0xFF43A6DB)
    val DayendColor = Color(0xFF2487BC)

    //Cloudy theme:
    val CloudystartColor = Color(0xFF91939A)
    val CloudyendColor = Color(0xFF585A5F)

    val gradient = when (imageRes) {
        R.drawable.clearsky_day /*"clearsky_night", "clearsky_polartwilight", "fair_day", "fair_night", "fair_polartwilight"*/
        -> {
            Brush.linearGradient(
                0.0f to DaystartColor,
                1.0f to DayendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        /*"lightsnowshowers_day", "lightsnowshowers_night", "lightsnowshowers_polartwilight",
        "heavysnowandthunder", "snow", "snowshowers_day", "snowshowers_night", "snowshowers_polartwilight",
        "heavysnowshowersandthunder_day", "heavysnowshowersandthunder_night", "heavysnowshowersandthunder_polartwilight",
        "heavysnowshowers_day", "heavysnowshowers_night", "heavysnowshowers_polartwilight",
        "snowshowersandthunder_day", "snowshowersandthunder_night", "snowshowersandthunder_polartwilight",
        "heavysnow", "sleet", "sleetshowers_day", "sleetshowers_night", "sleetshowers_polartwilight",
        "heavysleetshowersandthunder_day", "heavysleetshowersandthunder_night", "heavysleetshowersandthunder_polartwilight",
        "heavysleetshowers_day", "heavysleetshowers_night", "heavysleetshowers_polartwilight",
        "sleetshowersandthunder_day", "sleetshowersandthunder_night", "sleetshowersandthunder_polartwilight",
        "heavysleetandthunder", "lightsleet", "lightsleetshowersandthunder_day", "lightsleetshowersandthunder_night",
        "lightsleetshowersandthunder_polartwilight", "lightsleetshowers_day", "lightsleetshowers_night", "lightsleetshowers_polartwilight",
        */
        R.drawable.partlycloudy_day -> {
            Brush.linearGradient(
                0.0f to CloudystartColor,
                1.0f to CloudyendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        R.drawable.clearsky_day -> {
            Brush.linearGradient(
                0.0f to NighttimestartColor,
                1.0f to NighttimeendColor,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }

        R.drawable.clearsky_day/*, R.drawable.lightssnowshowersandthunder_polartwilight*/ -> {
            Brush.linearGradient(
                0.0f to EveningstartColor,
                1.0f to EveningendColor,
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