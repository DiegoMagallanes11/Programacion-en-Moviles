package com.tecsup.ayudacampus.ui.theme
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.tecsup.ayudacampus.model.Rol
@Composable fun AyudaTheme(rol: Rol = Rol.CLIENTE, oscuro: Boolean = false, content: @Composable () -> Unit) {
 val admin = rol == Rol.ADMIN
 val scheme = if (oscuro) darkColorScheme(
  primary = if(admin) Color(0xFFC4B5FD) else Color(0xFF67E8F9),
  primaryContainer = if(admin) Color(0xFF4C1D95) else Color(0xFF164E63),
  background = Color(0xFF10151D), surface = Color(0xFF161D28)
 ) else lightColorScheme(
  primary = if(admin) Violeta else Cian,
  primaryContainer = if(admin) Color(0xFFEDE4FF) else Color(0xFFCFFAFE),
  background = if(admin) Color(0xFFFAF8FF) else Color(0xFFF7FDFE),
  surface = Color.White
 )
 MaterialTheme(colorScheme=scheme, typography=AyudaTypography, content=content)
}
