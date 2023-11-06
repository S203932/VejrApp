package com.example.vejrapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vejrapp.presentation.day.DayPage
import com.example.vejrapp.presentation.day.DayViewModel
import com.example.vejrapp.presentation.day.DetailsBox
import com.example.vejrapp.presentation.day.TopWeather
import com.example.vejrapp.presentation.search.SearchBar
import com.example.vejrapp.presentation.search.SearchViewModel
import com.example.vejrapp.presentation.settings.DataViewModel
import com.example.vejrapp.presentation.settings.Settings
import com.example.vejrapp.presentation.theme.LinearGradient
import com.example.vejrapp.presentation.week.WeekView

@Composable
fun MainNavHost(
    navController: NavHostController,
    dataViewModel: DataViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    dayViewModel: DayViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Today.name,
//modifier = Modifier.padding(innerPadding)
    ) {


        composable(route = Route.Today.name) {
            LinearGradient()
            DayPage(navController, searchViewModel, dayViewModel)

        }
        composable(route = Route.Tomorrow.name) {

            LinearGradient()
            Column {
                SearchBar(
                    onNextButtonClicked = {
                        navController.navigate(Route.Settings.name)
                    },
                    navController = navController,
                    viewModel = searchViewModel

                )
                //Spacer(modifier = Modifier.height(100.dp))
                TopWeather()
                DetailsBox()


            }

        }

        composable(route = Route.Week.name) {

            // I think this is how to set up week view, not sure if you are supposed to use the
            // WeekWeather composable alone or with the card parameter

            //I just added the setting to that of the preview
            LinearGradient()
            Column {
                SearchBar(
                    onNextButtonClicked = {
                        navController.navigate(Route.Settings.name)
                    },
                    navController = navController,
                    viewModel = searchViewModel

                )
                Card {
                    WeekView()
                }


            }

        }

        composable(route = Route.Settings.name) {
            Settings(
                onNextButtonClicked = {
                    navController.previousBackStackEntry
                },
                onCancelButtonClicked = {
                    cancelAndNavigateToStart(navController)
                },
                modifier = Modifier.fillMaxHeight(),
                navController = navController,
                dataViewModel = dataViewModel
            )
        }
    }
}

private fun cancelAndNavigateToStart(navController: NavHostController) {
    navController.popBackStack(Route.Today.name, inclusive = false)
}