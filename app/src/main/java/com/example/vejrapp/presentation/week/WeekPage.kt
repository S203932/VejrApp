package com.example.vejrapp.presentation.week

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.presentation.search.SearchBar
import com.example.vejrapp.presentation.search.SearchViewModel

@Composable
@Preview
fun WeekPage(
    navController: NavHostController = rememberNavController(),
    searchViewModel: SearchViewModel = viewModel()
) {

    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(Route.Settings.name)
            },
            navController = navController,
            searchViewModel = searchViewModel
        )
        WeekView()
    }

}