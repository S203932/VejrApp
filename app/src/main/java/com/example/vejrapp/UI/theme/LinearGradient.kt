package com.example.vejrapp.UI.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Function to create the gradiant background in the app
@Composable
fun LinearGradient() {
    val gradient = Brush.linearGradient(
        00.0f to Color.Magenta,
        50.0f to Color.Cyan,
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier
            .background(gradient)
            .fillMaxSize()
    )
}