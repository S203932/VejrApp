package com.example.vejrapp.presentation.day

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vejrapp.navigation.Route
import com.example.vejrapp.presentation.search.DummySearchBar
import com.example.vejrapp.presentation.search.SearchBar
import com.example.vejrapp.presentation.search.SearchViewModel
import com.example.vejrapp.presentation.week.WeekView

@Composable
fun DayPage(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    dayViewModel: DayViewModel = viewModel()
) {

    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(Route.Settings.name)
            },
            navController = navController,
            viewModel = searchViewModel
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                TopWeather()
            }
            item {
                CautionBox()
            }
            item {
                LazyRowWithCards()
            }
            item {
                DetailsBox()
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        WeekView()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DayPreview() {
    Column {
        DummySearchBar()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                TopWeather()
            }
            item {
                CautionBox()
            }
            item {
                LazyRowWithCards()
            }
            item {
                DetailsBox()
            }
            item {
                Spacer(modifier = Modifier.height(6.dp))
                WeekView()
            }
        }


    }
}