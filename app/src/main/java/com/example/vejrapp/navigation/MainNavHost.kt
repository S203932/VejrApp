package com.example.vejrapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.example.vejrapp.presentation.week.WeekPage
import com.example.vejrapp.presentation.week.WeekView


@OptIn(ExperimentalFoundationApi::class)
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
        startDestination = Route.AllDaysAllWeek.name,
//modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.AllDaysAllWeek.name) {
            LinearGradient()
            val screens = listOf(
                Route.Today.name,
                Route.Tomorrow.name,
                Route.Week.name

            )
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                // provide pageCount
                3
            }
            val scope = rememberCoroutineScope()
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    key = { screens[it] },
                    pageSize = PageSize.Fill
                )
                { index ->
                    val route = screens[index]
                    when (route) {
                        Route.Today.name -> {

                            // Content specific to Today
                            DayPage(
                                navController = navController,
                                searchViewModel = searchViewModel,
                                dayViewModel = dayViewModel
                            )
                        }

                        Route.Tomorrow.name -> {
                            // Content specific to Tomorrow
                            DayPage(
                                navController = navController,
                                searchViewModel = searchViewModel,
                                dayViewModel = dayViewModel
                            )
                        }

                        Route.Week.name -> {
                            // Content specific to Tomorrow
                            WeekPage(
                                navController = navController,
                                searchViewModel = searchViewModel
                            )
                        }
                    }
                }
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .size(16.dp)
                        )
                    }
                }

            /*composable(route = Route.Today.name) {
            LinearGradient()
            DayPage(navController, searchViewModel, dayViewModel)
                        Route.Week.name -> {
                            // Content specific to Week
                            WeekPage(
                                navController = navController,
                                searchViewModel = searchViewModel
                            )
                        }
                    }
                }*/

                    /*Image(
                            painter = painterResource(id = screens[index]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )*/


                /*composable(route = Route.Today.name) {
    LinearGradient()
    DayPage(navController, searchViewModel)

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

}*/
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