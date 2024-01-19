package com.example.vejrapp.navigation

import androidx.annotation.StringRes
import com.example.vejrapp.R

// Routes used for navhost and pager
enum class Route(@StringRes val title: Int) {
    AllDaysAllWeek(title = R.string.all_days_all_week),
    Today(title = R.string.app_name),
    Settings(title = R.string.settings),
    Tomorrow(title = R.string.tomorrow),
    Week(title = R.string.week_view)
}