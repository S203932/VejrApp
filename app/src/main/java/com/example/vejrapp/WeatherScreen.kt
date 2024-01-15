package com.example.vejrapp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.navigation.MainNavHost


/*
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

 */

@SuppressLint("CoroutineCreationDuringComposition")
//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    MainNavHost(navController = navController)

}


/*
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

 */