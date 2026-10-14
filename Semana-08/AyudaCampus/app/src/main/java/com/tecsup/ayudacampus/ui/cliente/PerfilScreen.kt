package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.*
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.ui.components.*
@Composable fun PerfilScreen(vm: PerfilViewModel, sesion: SesionViewModel, usuario: Usuario, onCambiar: () -> Unit, onSalir: () -> Unit) {
 val resumen by vm.resumen.collectAsState();val oscuro by sesion.modoOscuro.collectAsState();val notificaciones by sesion.notificaciones.collectAsState();val error by sesion.error.collectAsState();val ocupado by sesion.ocupado.collectAsState()
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("Mi perfil",style=MaterialTheme.typography.headlineMedium)
  Icon(Icons.Default.AccountCircle,null,Modifier.size(64.dp),tint=MaterialTheme.colorScheme.primary)
  Text(usuario.nombre,style=MaterialTheme.typography.titleLarge);Text(usuario.correo);Text("Estudiante")
  Card(Modifier.fillMaxWidth()) { Row(Modifier.fillMaxWidth().padding(16.dp),horizontalArrangement=Arrangement.SpaceBetween) {
   listOf("Abiertos" to resumen.abiertos,"Resueltos" to resumen.resueltos,"Total" to resumen.total).forEach { (t,n) -> Column { Text("$n",style=MaterialTheme.typography.headlineSmall);Text(t) } }
  } }
  AjusteSwitch("Modo oscuro",oscuro,sesion::modoOscuro)
  AjusteSwitch("Notificaciones",notificaciones,sesion::notificaciones)
  Text("Modo práctica",style=MaterialTheme.typography.titleMedium);Text("Cambia de perfil para probar la atención de tus incidencias en este dispositivo.")
  AyudaButton("Cambiar a Soporte TI",!ocupado,onCambiar)
  OutlinedButton(onClick=onSalir,modifier=Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
  error?.let { Mensaje(it) }
 }
}
@Composable fun AjusteSwitch(texto: String, valor: Boolean, cambio: (Boolean) -> Unit) {
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) { Text(texto);Switch(checked=valor,onCheckedChange=cambio) }
}
