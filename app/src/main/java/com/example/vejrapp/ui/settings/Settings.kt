package com.example.vejrapp.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vejrapp.ui.settings.models.SettingsModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    val temperatureUnit = settingsViewModel.temperatureUnit.collectAsState()
    val windSpeedUnit = settingsViewModel.windSpeedUnit.collectAsState()
    val pressureUnit = settingsViewModel.pressureUnit.collectAsState()

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
            Setting(
                modifier,
                setting = temperatureUnit,
                checked = settingsViewModel.temperatureUnit.value.checked,
                onToggle = { settingsViewModel.toggleTemperatureUnit() })
            Setting(
                modifier,
                setting = windSpeedUnit,
                checked = settingsViewModel.windSpeedUnit.value.checked,
                onToggle = { settingsViewModel.toggleWindSpeedUnit() })
            Setting(
                modifier,
                setting = pressureUnit,
                checked = settingsViewModel.pressureUnit.value.checked,
                onToggle = { settingsViewModel.togglePressureUnit() })
        }
    }
}

@Composable
fun Setting(
    modifier: Modifier = Modifier,
    setting: State<SettingsModel>,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = setting.value.name,
            fontSize = 35.sp,
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.2f)
                .size(50.dp)
        )

        Text(
            text = setting.value.getSelected().toString(),
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .fillMaxHeight(0.2f),
        )

        Switch(checked = setting.value.checked, onCheckedChange = { onToggle() })
    }
}
