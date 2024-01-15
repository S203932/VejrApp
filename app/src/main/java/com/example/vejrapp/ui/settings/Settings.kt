package com.example.vejrapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vejrapp.ui.settings.models.SettingsModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
) {
    val temperatureUnit by settingsViewModel.temperatureUnit.collectAsState()
    val windSpeedUnit by settingsViewModel.windSpeedUnit.collectAsState()
    val pressureUnit by settingsViewModel.pressureUnit.collectAsState()

    println(temperatureUnit.toString())

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            IconButton(
                onClick = {
                    navController.popBackStack()
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
        }, bottomBar = { About() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            Setting(
                setting = temperatureUnit,
                onToggle = { settingsViewModel.toggleTemperatureUnit() })
            Setting(
                setting = windSpeedUnit,
                onToggle = { settingsViewModel.toggleWindSpeedUnit() })
            Setting(
                setting = pressureUnit,
                onToggle = { settingsViewModel.togglePressureUnit() })
        }
    }
}

@Composable
fun Setting(
    setting: SettingsModel,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = setting.name,
                fontSize = 35.sp
            )
            Text(
                text = setting.getSelected(),
                fontSize = 20.sp
            )
        }
        Switch(checked = setting.checked, onCheckedChange = { onToggle() })
    }
}

@Composable
fun About() {
    Column(
        modifier = Modifier
            .padding(8.dp)

    ) {
        Text(text = "About", fontSize = 24.sp)
        Text(text = "VejrApp is created by")
        Text(text = "Daniel, Kristian, Markus, Natali og Patrick")
    }
}
