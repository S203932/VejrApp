package com.example.vejrapp

//import androidx.compose.foundation.layout.RowScopeInstance.align
//import com.example.vejrapp.SearchInputStateImpl.Companion.Saver
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.vejrapp.ui.theme.VejrAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VejrAppTheme {
                WeatherApp()
            }
        }
    }
}


