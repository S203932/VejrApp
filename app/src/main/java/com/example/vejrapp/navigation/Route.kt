package com.example.vejrapp.navigation

import androidx.annotation.StringRes
import com.example.vejrapp.R

enum class Route(@StringRes val title: Int) {
    Today(title = R.string.app_name),
    Settings(title = R.string.settings),
    Tomorrow(title = R.string.tomorrow),
    Week(title = R.string.week_view)
}