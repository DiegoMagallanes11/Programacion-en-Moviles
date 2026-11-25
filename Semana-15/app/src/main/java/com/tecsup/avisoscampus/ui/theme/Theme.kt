package com.tecsup.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colores = lightColorScheme(
    primary = Color(0xFF2055A6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE8FF),
    background = Color(0xFFF3F6FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE7ECF5)
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colores, content = content)
}
