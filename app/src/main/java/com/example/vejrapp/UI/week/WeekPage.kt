package com.example.vejrapp.UI.week

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.UI.search.SearchBar
import com.example.vejrapp.UI.search.SearchViewModel

@Composable
fun WeekPage(
    navController: NavHostController = rememberNavController(),
    searchViewModel: SearchViewModel = viewModel(),
    weekViewModel: WeekViewModel = viewModel(),
) {

    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(Route.Settings.name)
            },
            navController = navController,
            searchViewModel = searchViewModel
        )
        WeekView(
            weekViewModel = weekViewModel,
        )
    }

}