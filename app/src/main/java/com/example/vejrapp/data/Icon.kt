package com.example.vejrapp.data
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.vejrapp.R
// Used to resize the icons of the weather conditions
fun cropBitmap(originalBitmap: Bitmap): Bitmap {
    val width = originalBitmap.width
    val height = originalBitmap.height

    var left = 0
    var top = 0
    var right = width
    var bottom = height

    // Find left edge
    while (left < width && isColumnTransparent(originalBitmap, left)) {
        left++
    }

    // Find top edge
    while (top < height && isRowTransparent(originalBitmap, top)) {
        top++
    }

    // Find right edge
    while (right > left && isColumnTransparent(originalBitmap, right - 1)) {
        right--
    }

    // Find bottom edge
    while (bottom > top && isRowTransparent(originalBitmap, bottom - 1)) {
        bottom--
    }

    // Create a new Bitmap with the cropped area
    val croppedBitmap = Bitmap.createBitmap(originalBitmap, left, top, right - left, bottom - top)

    return croppedBitmap
}
private fun isColumnTransparent(bitmap: Bitmap, column: Int): Boolean {
    for (row in 0 until bitmap.height) {
        if (bitmap.getPixel(column, row) != 0) {
            return false
        }
    }
    return true
}

private fun isRowTransparent(bitmap: Bitmap, row: Int): Boolean {
    for (column in 0 until bitmap.width) {
        if (bitmap.getPixel(column, row) != 0) {
            return false
        }
    }
    return true
}

fun getBitmapFromImage(context: Context, drawable: Int): Bitmap {
    val db = ContextCompat.getDrawable(context, drawable)

    //create bitmap
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )

    // create a variable for canvas.
    val canvas = Canvas(bit)

    //set bounds for bitmap.
    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)

    // return bitmap.
    return bit
}
// Mapping the given icon to an image
@DrawableRes
fun String.mapToYRImageResource(): Int =
    when (this) {
        "clearsky_day" -> {
            R.drawable.clearsky_day
        }

        "clearsky_night" -> {
            R.drawable.clearsky_night
        }

        "clearsky_polartwilight" -> {
            R.drawable.clearsky_polartwilight
        }

        "fair_day" -> {
            R.drawable.fair_day
        }

        "fair_night" -> {
            R.drawable.fair_night
        }

        "fair_polartwilight" -> {
            R.drawable.fair_polartwilight
        }

        "lightssnowshowersandthunder_day" -> {
            R.drawable.lightrainshowersandthunder_day
        }

        "lightssnowshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssnowshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "lightsnowshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightsnowshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightsnowshowers_polartwilight" -> {
            R.drawable.lightsnowshowers_polartwilight
        }

        "heavyrainandthunder" -> {
            R.drawable.heavyrainandthunder
        }

        "heavysnowandthunder" -> {
            R.drawable.heavysnowandthunder
        }

        "rainandthunder" -> {
            R.drawable.rainandthunder
        }

        "heavysleetshowersandthunder_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowersandthunder_night" -> {
            R.drawable.heavysleetshowersandthunder_night
        }

        "heavysleetshowersandthunder_polartwilight" -> {
            R.drawable.heavysleetshowersandthunder_polartwilight
        }

        "heavysnow" -> {
            R.drawable.heavysnow
        }

        "heavyrainshowers_day" -> {
            R.drawable.heavyrainshowers_day
        }

        "heavyrainshowers_night" -> {
            R.drawable.heavyrainshowers_night
        }

        "heavyrainshowers_polartwilight" -> {
            R.drawable.heavyrainshowers_polartwilight
        }

        "lightsleet" -> {
            R.drawable.lightsleet
        }

        "heavyrain" -> {
            R.drawable.heavyrain
        }

        "lightrainshowers_day" -> {
            R.drawable.lightrainshowers_day
        }

        "lightrainshowers_night" -> {
            R.drawable.lightrainshowers_night
        }

        "lightrainshowers_polartwilight" -> {
            R.drawable.lightrainshowers_polartwilight
        }

        "heavysleetshowers_day" -> {
            R.drawable.heavysleetshowers_day
        }

        "heavysleetshowers_night" -> {
            R.drawable.heavysleetshowers_night
        }

        "heavysleetshowers_polartwilight" -> {
            R.drawable.heavysleetshowers_polartwilight
        }

        "lightsleetshowers_day" -> {
            R.drawable.lightsleetshowers_day
        }

        "lightsleetshowers_night" -> {
            R.drawable.lightsleetshowers_night
        }

        "lightsleetshowers_polartwilight" -> {
            R.drawable.lightsleetshowers_polartwilight
        }

        "snow" -> {
            R.drawable.snow
        }

        "heavyrainshowersandthunder_day" -> {
            R.drawable.heavyrainshowersandthunder_day
        }

        "heavyrainshowersandthunder_night" -> {
            R.drawable.heavyrainshowersandthunder_night
        }

        "heavyrainshowersandthunder_polartwilight" -> {
            R.drawable.heavyrainshowersandthunder_polartwilight
        }

        "snowshowers_day" -> {
            R.drawable.snowshowers_day
        }

        "snowshowers_night" -> {
            R.drawable.snowshowers_night
        }

        "snowshowers_polartwilight" -> {
            R.drawable.snowshowers_polartwilight
        }

        "fog" -> {
            R.drawable.fog
        }

        "snowshowersandthunder_day" -> {
            R.drawable.snowshowersandthunder_day
        }

        "snowshowersandthunder_night" -> {
            R.drawable.snowshowersandthunder_night
        }

        "snowshowersandthunder_polartwilight" -> {
            R.drawable.snowshowersandthunder_polartwilight
        }

        "lightsnowandthunder" -> {
            R.drawable.lightsnowandthunder
        }

        "heavysleetandthunder" -> {
            R.drawable.heavysleetandthunder
        }

        "lightrain" -> {
            R.drawable.lightrain
        }

        "rainshowersandthunder_day" -> {
            R.drawable.rainshowersandthunder_day
        }

        "rainshowersandthunder_night" -> {
            R.drawable.rainshowersandthunder_night
        }

        "rainshowersandthunder_polartwilight" -> {
            R.drawable.rainshowersandthunder_polartwilight
        }

        "rain" -> {
            R.drawable.rain
        }

        "lightsnow" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_day" -> {
            R.drawable.lightsnow
        }

        "lightrainshowersandthunder_night" -> {
            R.drawable.lightrainshowersandthunder_night
        }

        "lightrainshowersandthunder_polartwilight" -> {
            R.drawable.lightrainshowersandthunder_polartwilight
        }

        "heavysleet" -> {
            R.drawable.heavysleet
        }

        "sleetandthunder" -> {
            R.drawable.sleetandthunder
        }

        "lightrainandthunder" -> {
            R.drawable.lightrainandthunder
        }

        "sleet" -> {
            R.drawable.sleet
        }

        "lightssleetshowersandthunder_day" -> {
            R.drawable.lightssleetshowersandthunder_day
        }

        "lightssleetshowersandthunder_night" -> {
            R.drawable.lightssnowshowersandthunder_night
        }

        "lightssleetshowersandthunder_polartwilight" -> {
            R.drawable.lightssleetshowersandthunder_polartwilight
        }

        "lightsleetandthunder" -> {
            R.drawable.lightsleetandthunder
        }

        "partlycloudy_day" -> {
            R.drawable.partlycloudy_day
        }

        "partlycloudy_night" -> {
            R.drawable.partlycloudy_night
        }

        "partlycloudy_polartwilight" -> {
            R.drawable.partlycloudy_polartwilight
        }

        "sleetshowersandthunder_day" -> {
            R.drawable.sleetshowersandthunder_day
        }

        "sleetshowersandthunder_night" -> {
            R.drawable.sleetshowersandthunder_night
        }

        "sleetshowersandthunder_polartwilight" -> {
            R.drawable.sleetshowersandthunder_polartwilight
        }

        "rainshowers_day" -> {
            R.drawable.rainshowers_day
        }

        "rainshowers_night" -> {
            R.drawable.rainshowers_night
        }

        "rainshowers_polartwilight" -> {
            R.drawable.rainshowers_polartwilight
        }

        "snowandthunder" -> {
            R.drawable.snowandthunder
        }

        "sleetshowers_day" -> {
            R.drawable.sleetshowers_day
        }

        "sleetshowers_night" -> {
            R.drawable.sleetshowers_night
        }

        "sleetshowers_polartwilight" -> {
            R.drawable.sleetshowers_polartwilight
        }

        "cloudy" -> {
            R.drawable.cloudy
        }

        "heavysnowshowersandthunder_day" -> {
            R.drawable.heavysnowshowersandthunder_day
        }

        "heavysnowshowersandthunder_night" -> {
            R.drawable.heavysnowshowersandthunder_night
        }

        "heavysnowshowersandthunder_polartwilight" -> {
            R.drawable.heavysnowshowersandthunder_polartwilight
        }

        "heavysnowshowers_day" -> {
            R.drawable.heavysnowshowers_day
        }

        "heavysnowshowers_night" -> {
            R.drawable.heavysnowshowers_night
        }

        "heavysnowshowers_polartwilight" -> {
            R.drawable.heavysnowshowers_polartwilight
        }

        else -> {
            R.drawable.cloudy
        }
    }
