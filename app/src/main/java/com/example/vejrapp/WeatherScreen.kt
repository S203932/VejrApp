package com.example.vejrapp

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.appscreens.DayPage
import com.example.vejrapp.data.SearchViewModel

enum class WeatherScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Settings(title = R.string.settings),
    Tomorrow(title = R.string.tomorrow),
    Weekview(title = R.string.week_view)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    currentScreen: WeatherScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WeatherApp(
    navController: NavHostController = rememberNavController(),
    dataViewModel: DataViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {
//    val dataViewModel = viewModel<DataViewModel>()
//    val searchViewModel = viewModel<SearchViewModel>()
//    val searchViewModel: SearchViewModel by viewModels()
//    val searchViewModel = SearchViewModel by viewModels()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = WeatherScreen.valueOf(
        backStackEntry?.destination?.route ?: WeatherScreen.Start.name
    )


    //Scaffold(

    /*topBar = {
        /* WeatherAppBar(
             currentScreen = currentScreen,
             canNavigateBack = navController.previousBackStackEntry != null,
             navigateUp = { navController.navigateUp() }
         )*/


    }

     */


    // ) { innerPadding ->

    NavHost(
        navController = navController,
        startDestination = WeatherScreen.Start.name,
        //modifier = Modifier.padding(innerPadding)
    ) {


        composable(route = WeatherScreen.Start.name) {
            LinearGradient()
            DayPage(navController, searchViewModel)

        }
        composable(route = WeatherScreen.Tomorrow.name) {

            LinearGradient()
            Column {
                SearchBar(
                    onNextButtonClicked = {
                        navController.navigate(WeatherScreen.Settings.name)
                    },
                    navController = navController,
                    viewModel = searchViewModel

                )
                //Spacer(modifier = Modifier.height(100.dp))
                TopWeather()
                DetailsBox()


            }

        }

        composable(route = WeatherScreen.Weekview.name) {

            // I think this is how to set up week view, not sure if you are supposed to use the
            // WeekWeather composable alone or with the card parameter

            //I just added the setting to that of the preview
            LinearGradient()
            Column {
                SearchBar(
                    onNextButtonClicked = {
                        navController.navigate(WeatherScreen.Settings.name)
                    },
                    navController = navController,
                    viewModel = searchViewModel

                )
                Card {
                    WeekView()
                }


            }

        }

        composable(route = WeatherScreen.Settings.name) {
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
//}


private fun cancelAndNavigateToStart(navController: NavHostController) {
    navController.popBackStack(WeatherScreen.Start.name, inclusive = false)
}

@Composable
fun LinearGradient() {
    val gradient = Brush.linearGradient(
        00.0f to Color.Magenta,
        50.0f to Color.Cyan,
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize()
    )
}
