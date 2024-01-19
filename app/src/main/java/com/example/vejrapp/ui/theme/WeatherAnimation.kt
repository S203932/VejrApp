package com.example.vejrapp.ui.theme

//import coil.ImageLoader
import android.app.Activity
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.vejrapp.data.repository.WeatherUtils.getCurrentIndex
import com.example.vejrapp.data.repository.models.WeatherData
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

/*
Weather animation uses Matteo Battilana's Android library
com.github.matteobattilana.weather
To help make the animations

The Gifsplash, CountDownScreen, and SplashScreen
are also taken and modified from ygorluizfrazao's
github release (compose-splash-screens), but not
used directly as a dependency (like the weather animations)
as its source code breaks our own

 */

@Composable
fun WeatherAnimation(weatherData: WeatherData, dayInt: Int) {

    val dataCurrentHour = weatherData.data.days[dayInt].hours[getCurrentIndex(weatherData, 0)].data
    //Takes the symbolcode string from the weather api that is used to pick weather icons
    val weatherState = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()

    if (weatherState.contains("rain")) {
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.RAIN) }
            })
    } else if (weatherState.contains("snow")) {
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.SNOW) }
            })
    } else if (weatherState.contains("sleet")) {
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.SNOW) }
            })
        AndroidView(
            factory = { context ->
                WeatherView(context, null).apply { this.setWeatherData(PrecipType.RAIN) }
            })
    }


}

@Composable
fun GifSplash(
    modifier: Modifier = Modifier,
    @DrawableRes gifImage: Int,
    gifSize: Size = Size.ORIGINAL,
    contentDescription: String,
    context: Context = LocalContext.current,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current
) {
    //Uses coils gif support to load a gif file
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(data = gifImage).apply(block = {
            size(gifSize)
        }).build(), imageLoader = imageLoader
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription
        )
        Text(text = text, style = textStyle, overflow = TextOverflow.Ellipsis)
    }
}


//This creates the countdown timer the splash screen is available
@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    totalTimeInMillis: Long = 1100,
    notifyMeEveryMillis: Long = 100,
    onNotify: () -> Unit = {},
    beforeFinished: @Composable BoxScope.() -> Unit,
    whenFinished: @Composable () -> Unit
) {

    var finished by rememberSaveable {
        mutableStateOf(false)
    }

    SplashScreen(
        modifier = modifier,
        finished = finished,
        beforeFinished = beforeFinished,
        whenFinished = whenFinished
    )

    LaunchedEffect(key1 = true) {
        object : CountDownTimer(totalTimeInMillis, notifyMeEveryMillis) {
            override fun onTick(millisUntilFinished: Long) {
                onNotify()
            }

            override fun onFinish() {
                finished = true
            }
        }.start()
    }
}


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    finished: Boolean,
    beforeFinished: @Composable BoxScope.() -> Unit,
    whenFinished: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {

        val view = LocalView.current
        if (!finished) {
            if (!view.isInEditMode) {
                val currentWindow = (view.context as? Activity)?.window
                currentWindow?.let {
                    SideEffect {
                        WindowCompat.getInsetsController(it, view)
                            .hide(WindowInsetsCompat.Type.statusBars())
                    }
                }
            }

            beforeFinished()
        } else {

            if (!view.isInEditMode) {
                val currentWindow = (view.context as? Activity)?.window
                currentWindow?.let {
                    WindowCompat.getInsetsController(it, view)
                        .show(WindowInsetsCompat.Type.statusBars())
                }
            }

            whenFinished()
        }

    }
}