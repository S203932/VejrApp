package com.example.vejrapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vejrapp.data.SearchViewModel
import com.example.vejrapp.ui.theme.VejrAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navController: NavController,
    dataViewModel: DataViewModel,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val tempUnit = dataViewModel.tempUnit.collectAsState()
    val windUnit = dataViewModel.windUnit.collectAsState()
    val pressureUnit = dataViewModel.pressureUnit.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(values)

        ) {
            IconButton(

                onClick = {
                    navController.popBackStack()
                    //onNextButtonClicked

                }
            ) {
                Icon(

                    modifier = Modifier
                        .padding(2.dp, 2.dp)
                        .size(30.dp)
                        .background(color = Color.Transparent, shape = CircleShape),


                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )

            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "Temperature",
                    fontSize = 35.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(0.2f)
                        .size(50.dp)
                )

                Text(
                    text = "C/F",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .fillMaxHeight(0.2f),
                )


                var checked by remember { mutableStateOf(tempUnit.value == "Fahrenheit") }
                Switch(

                    checked =
                    checked,
                    onCheckedChange = {
                        if (tempUnit.value == ("Celsius")) dataViewModel.updateTempUnit("Fahrenheit")
                        else dataViewModel.updateTempUnit("Celsius")
                        //dataViewModel.updateTempUnit("Fahrenheit")
                        checked = !checked

                    }
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "Wind",
                    fontSize = 35.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(0.2f)
                        .size(50.dp)
                )

                Text(
                    text = "m/s\nf/s",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .fillMaxHeight(0.2f),
                )

                var checked by remember { mutableStateOf(windUnit.value == "f/s") }
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        if (windUnit.value == ("m/s")) dataViewModel.updateWindUnit("f/s")
                        else dataViewModel.updateWindUnit("m/s")
                        checked = !checked
                    }
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "Pressure",
                    fontSize = 35.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(0.2f)
                        .size(50.dp)
                )

                Text(
                    text = "Bar/\nPa",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .fillMaxHeight(0.2f),
                )

                var checked by remember { mutableStateOf(pressureUnit.value == "Pa") }
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        if (pressureUnit.value == ("Bar")) dataViewModel.updatePressureUnit("Pa")
                        else dataViewModel.updatePressureUnit("Bar")
                        checked = !checked
                    }
                )

            }


        }


    }


}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    VejrAppTheme {
        val navController = rememberNavController()
        val dataViewModel = viewModel<DataViewModel>()
        Settings(navController = navController, dataViewModel)
    }
}