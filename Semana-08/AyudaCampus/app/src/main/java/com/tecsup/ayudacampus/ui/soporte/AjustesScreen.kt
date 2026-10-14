package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import com.tecsup.ayudacampus.ui.cliente.AjusteSwitch
import com.tecsup.ayudacampus.ui.components.*
@Composable fun AjustesScreen(sesion: SesionViewModel, onCambiar: () -> Unit, onSalir: () -> Unit) {
 val oscuro by sesion.modoOscuro.collectAsState();val recibir by sesion.recibirTickets.collectAsState();val notificaciones by sesion.notificaciones.collectAsState()
 val usuario by sesion.usuarioActual.collectAsState();val error by sesion.error.collectAsState();val ocupado by sesion.ocupado.collectAsState()
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("Ajustes",style=MaterialTheme.typography.headlineMedium)
  Text(usuario?.nombre ?: "Soporte TI",style=MaterialTheme.typography.titleLarge);Text(usuario?.correo ?: "")
  AjusteSwitch("Modo oscuro",oscuro,sesion::modoOscuro)
  AjusteSwitch("Notificaciones",notificaciones,sesion::notificaciones)
  AjusteSwitch("Recibir tickets",recibir,sesion::recibirTickets)
  if(!recibir) Text("Mesa de ayuda cerrada: los estudiantes no pueden crear tickets.")
  HorizontalDivider();Text("Modo práctica",style=MaterialTheme.typography.titleMedium)
  AyudaButton("Cambiar a Estudiante",!ocupado,onCambiar)
  OutlinedButton(onClick=onSalir,modifier=Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
  error?.let { Mensaje(it) }
 }
}
