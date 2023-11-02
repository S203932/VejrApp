package com.example.vejrapp.appscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.SearchBar
import com.example.vejrapp.WeatherScreen
import com.example.vejrapp.WeekView
import com.example.vejrapp.data.SearchViewModel

@Composable
@Preview
fun WeekPage(
    navController: NavHostController = rememberNavController(),
    searchViewModel: SearchViewModel = viewModel()
) {

    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(WeatherScreen.Settings.name)
            },
            navController = navController,
            viewModel = searchViewModel
        )
        WeekView()
    }

}