package com.example.vejrapp.ui.settings.models

data class SettingModel(
    val name: String,
    val choices: Map<Boolean, String>,
    val choiceUnit: String,
    var checked: Boolean = false
) {
    // false is the default choice
    fun getSelected(): String {
        return choices[checked].toString()
    }
}