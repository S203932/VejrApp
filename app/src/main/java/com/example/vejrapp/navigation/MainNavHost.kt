package com.example.vejrapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vejrapp.ui.screens.Day
import com.example.vejrapp.ui.screens.ScreenViewModel
import com.example.vejrapp.ui.screens.WeekPage
import com.example.vejrapp.ui.search.SearchBar
import com.example.vejrapp.ui.search.SearchViewModel
import com.example.vejrapp.ui.settings.Settings
import com.example.vejrapp.ui.settings.SettingsViewModel
import com.example.vejrapp.ui.theme.LinearGradient
import java.time.LocalDateTime


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val screenViewModel = hiltViewModel<ScreenViewModel>()
    val searchViewModel = hiltViewModel<SearchViewModel>()

    NavHost(
        navController = navController,
        startDestination = Route.AllDaysAllWeek.name,
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
                screens.size
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    SearchBar(navController = navController, searchViewModel = searchViewModel)
                    HorizontalPager(
                        state = pagerState,
                        key = { screens[it] },
                        pageSize = PageSize.Fill,
                        //Quickfix for lag
                        // beyondBoundsPageCount = screens.size - 1,
                        verticalAlignment = Alignment.Top,
                    ) { index ->
                        when (screens[index]) {
                            Route.Today.name -> {
                                // Content specific to Today
                                Day(
                                    navController = navController,
                                    screenViewModel = screenViewModel,
                                    LocalDateTime.now()
                                )
                            }

                            Route.Tomorrow.name -> {
                                // Content specific to Tomorrow
                                Day(
                                    navController = navController,
                                    screenViewModel = screenViewModel,
                                    LocalDateTime.now().plusDays(1)
                                )
                            }

                            Route.Week.name -> {
                                // Content specific to Week
                                WeekPage(
                                    navController = navController,
                                    screenViewModel = screenViewModel

                                )
                            }
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
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
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
                settingsViewModel = settingsViewModel,
                navController = navController,
            )
        }
    }
}