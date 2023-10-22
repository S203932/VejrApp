package com.example.vejrapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.data.SearchViewModel
import javax.sql.DataSource


enum class WeatherScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Settings(title = R.string.settings)
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
@Composable
fun WeatherApp(
    //dataViewModel: DataViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
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
        val uiState by dataViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = WeatherScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(route = WeatherScreen.Start.name) {
                StartScreen(
                    viewModel = searchViewModel,
                    onNextButtonClicked = {
                        //viewModel.setQuantity(it)
                        navController.navigate(WeatherScreen.Settings.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding()
                )
            }

            composable(route = WeatherScreen.Settings.name) {
                val context = LocalContext.current

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
                    //canNavigateBack = true,
                    //currentScreen = WeatherScreen.Settings
                )
            }
            /*
            composable(route = WeatherScreen.Settings.name) {
                val context = LocalContext.current
                Settings(
                    onNextButtonClicked = { navController.navigate(WeatherScreen.Start.name) },
                    onCancelButtonClicked = {
                        cancelAndNavigateToStart(viewModel, navController)
                    },
                    //options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    //onSelectionChanged = { viewModel.setFlavor(it) },
                    //modifier = Modifier.fillMaxHeight()
                )
            }

             */
        }
        /*
        composable(route = CupcakeScreen.Pickup.name) {
            SelectOptionScreen(
                subtotal = uiState.price,
                onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                onCancelButtonClicked = {
                    cancelOrderAndNavigateToStart(viewModel, navController)
                },
                options = uiState.pickupOptions,
                onSelectionChanged = { viewModel.setDate(it) },
                modifier = Modifier.fillMaxHeight()
            )
        }
        composable(route = CupcakeScreen.Summary.name) {
            val context = LocalContext.current
            OrderSummaryScreen(
                orderUiState = uiState,
                onCancelButtonClicked = {
                    cancelOrderAndNavigateToStart(viewModel, navController)
                },
                onSendButtonClicked = { subject: String, summary: String ->
                    shareOrder(context, subject = subject, summary = summary)
                },
                modifier = Modifier.fillMaxHeight()
            )
        }*/
    }
}


private fun cancelAndNavigateToStart(
    viewModel: DataViewModel,
    navController: NavHostController
) {
    navController.popBackStack(WeatherScreen.Start.name, inclusive = false)
}
