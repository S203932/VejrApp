package com.example.vejrapp.appscreens

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DummySearchBar() {
    val fontColor = Color.Black
    TopAppBar(
        title = {
            TextField(
                value = "Copenhagen",
                onValueChange = {},
                modifier = Modifier
                    .width(345.dp)
                    .motionEventSpy {}
                    .onFocusEvent {},
                placeholder = { Text(text = "Copenhagen", color = fontColor) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search, contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                },
                shape = RoundedCornerShape(25.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                textStyle = androidx.compose.ui.text.TextStyle(color = fontColor, fontSize = 20.sp),
                colors = textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Open Settings"
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
    )
}
