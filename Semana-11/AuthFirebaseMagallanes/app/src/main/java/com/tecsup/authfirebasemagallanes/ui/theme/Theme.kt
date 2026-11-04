package com.tecsup.authfirebasemagallanes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LabColors = lightColorScheme(
    primary = Color(0xFF6650A4),
    onPrimary = Color.White,
    secondary = Color(0xFF625B71),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun AuthFirebaseMagallanesTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LabColors, typography = Typography, content = content)
}
