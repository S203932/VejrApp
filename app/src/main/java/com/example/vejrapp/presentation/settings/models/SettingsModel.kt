package com.example.vejrapp.presentation.settings.models

import androidx.collection.CircularArray
import java.util.LinkedList

data class SettingsModel(val name:String, val choices:Map<Boolean,String>, var checked: Boolean = false){
    fun getSelected(): String {
        return choices[checked].toString()
    }
}
