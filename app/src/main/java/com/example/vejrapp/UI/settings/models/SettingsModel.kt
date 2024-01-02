package com.example.vejrapp.UI.settings.models

data class SettingsModel(
    val name: String,
    val choices: Map<Boolean, String>,
    var checked: Boolean = false
) {
    fun getSelected(): String {
        return choices[checked].toString()
    }
}