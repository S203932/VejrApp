package com.example.vejrapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vejrapp.UI.day.DayPage
import com.example.vejrapp.UI.day.DayViewModel
import com.example.vejrapp.UI.search.SearchViewModel
import com.example.vejrapp.UI.settings.Settings
import com.example.vejrapp.UI.settings.SettingsViewModel
import com.example.vejrapp.UI.theme.LinearGradient
import com.example.vejrapp.UI.week.WeekPage
import com.example.vejrapp.UI.week.WeekViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
    dayViewModel: DayViewModel = viewModel(),
    weekViewModel: WeekViewModel = viewModel(),

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
                //Route.Tomorrow.name,
                Route.Week.name

            )
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                // provide pageCount
                2
            }
            val scope = rememberCoroutineScope()
            Box(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    key = { screens[it] },
                    pageSize = PageSize.Fill
                ) { index ->
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
                        /*
                        Route.Tomorrow.name -> {
                            // Content specific to Tomorrow
                            DayPage(
                                navController = navController,
                                searchViewModel = searchViewModel,
                                dayViewModel = dayViewModel
                            )
                        }

                         */

                        Route.Week.name -> {
                            // Content specific to Week
                            WeekPage(
                                navController = navController,
                                searchViewModel = searchViewModel,
                                weekViewModel = weekViewModel
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .offset(y = -(16).dp)
                        .clip(RoundedCornerShape(100))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .size(8.dp)
                                    .background(color)
                            )
                        }
                    }
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
                settingsViewModel = settingsViewModel
            )
        }
    }
}

private fun cancelAndNavigateToStart(navController: NavHostController) {
    navController.popBackStack(Route.Today.name, inclusive = false)
}