package com.example.vejrapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.navigation.MainNavHost
import com.example.vejrapp.navigation.Route


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    currentScreen: Route,
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


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Route.valueOf(
        backStackEntry?.destination?.route ?: Route.Today.name
    )

    MainNavHost(navController = navController)
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
}


@Composable
fun PictureBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bluefigma), // Use your own image resource
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
