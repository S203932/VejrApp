package com.example.vejrapp.ui.theme

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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vejrapp.data.repository.models.WeatherData
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

@Composable
fun WeatherAnimation(weatherData: WeatherData) {
    val dataCurrentHour = weatherData.data.days[0].hours[0].data
    val weatherState = dataCurrentHour.nextOneHours?.summary?.symbolCode.toString()
    val weatherType: PrecipType
    //    var sleet: Boolean

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