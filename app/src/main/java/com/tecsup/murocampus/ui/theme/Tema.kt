package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colores = lightColorScheme(
    primary = Color(0xFF244C86),
    onPrimary = Color.White,
    secondaryContainer = Color(0xFFD9E6FA),
    onSecondaryContainer = Color(0xFF173761),
    background = Color(0xFFF4F7FB),
    surface = Color(0xFFFCFCFF),
    onSurface = Color(0xFF162234)
)

@Composable
fun MuroCampusTheme(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colores, content = contenido)
}
