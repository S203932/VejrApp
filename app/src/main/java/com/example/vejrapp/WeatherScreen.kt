package com.example.vejrapp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.navigation.MainNavHost

@SuppressLint("CoroutineCreationDuringComposition")
//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    MainNavHost(navController = navController)
}
