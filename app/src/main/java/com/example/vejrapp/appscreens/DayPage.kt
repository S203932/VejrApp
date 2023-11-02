package com.example.vejrapp.appscreens

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
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.CautionBox
import com.example.vejrapp.DetailsBox
import com.example.vejrapp.LazyRowWithCards
import com.example.vejrapp.SearchBar
import com.example.vejrapp.TopWeather
import com.example.vejrapp.WeatherScreen
import com.example.vejrapp.WeekView
import com.example.vejrapp.data.SearchViewModel

@Composable
fun DayPage(
    navController: NavHostController = rememberNavController(),
    searchViewModel: SearchViewModel = viewModel()
) {
    Column {
        SearchBar(
            onNextButtonClicked = {
                navController.navigate(WeatherScreen.Settings.name)
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
            item {
                Spacer(modifier = Modifier.height(6.dp))
                WeekView()
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DayPreview(){
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