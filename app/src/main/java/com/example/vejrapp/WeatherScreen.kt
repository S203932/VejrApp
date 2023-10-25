package com.example.vejrapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.data.SearchViewModel


enum class WeatherScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Settings(title = R.string.settings),
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
    navController: NavHostController = rememberNavController()
) {
    val dataViewModel = viewModel<DataViewModel>()

    val searchViewModel = viewModel<SearchViewModel>()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = WeatherScreen.valueOf(
        backStackEntry?.destination?.route ?: WeatherScreen.Start.name
    )

    Scaffold(
        /*
        topBar = {
            WeatherAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }

         */


    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = WeatherScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(route = WeatherScreen.Start.name) {
                SearchBar(
                    viewModel = searchViewModel,

                    onNextButtonClicked = {
                        navController.navigate(WeatherScreen.Settings.name)
                    },
                    navController = navController
                )

            }

            composable(route = WeatherScreen.Settings.name) {
                Settings(
                    onNextButtonClicked = {
                        navController.previousBackStackEntry
                    },
                    onCancelButtonClicked = {
                        cancelAndNavigateToStart(dataViewModel, navController)
                    },
                    navController = navController,

                    modifier = Modifier.fillMaxHeight(),

                    dataViewModel = dataViewModel,
                )
            }
        }
    }
}


private fun cancelAndNavigateToStart(
    viewModel: DataViewModel,
    navController: NavHostController
) {
    navController.popBackStack(WeatherScreen.Start.name, inclusive = false)
}
