package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.semantics.Role
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.tecsup.ayudacampus.viewmodel.*
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.ui.components.*
import com.tecsup.ayudacampus.ui.theme.*
@Composable fun PerfilScreen(vm: PerfilViewModel, sesion: SesionViewModel, usuario: Usuario, onCambiar: () -> Unit, onSalir: () -> Unit) {
 val resumen by vm.resumen.collectAsState();val oscuro by sesion.modoOscuro.collectAsState();val notificaciones by sesion.notificaciones.collectAsState();val error by sesion.error.collectAsState();val ocupado by sesion.ocupado.collectAsState()
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("Mi perfil",style=MaterialTheme.typography.headlineMedium)
  Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
   Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(16.dp)) {
     Icon(Icons.Default.AccountCircle,null,Modifier.size(56.dp),tint=MaterialTheme.colorScheme.primary)
     Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)) {
      Text(usuario.nombre,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.SemiBold)
      Text("Estudiante",style=MaterialTheme.typography.labelLarge)
     }
    }
    Column(verticalArrangement=Arrangement.spacedBy(4.dp)) {
     Text("Correo de acceso de la cuenta de práctica",style=MaterialTheme.typography.labelSmall)
     Text(usuario.correo,style=MaterialTheme.typography.bodyMedium)
    }
   }
  }
  Text("Mis tickets · ${resumen.total} en total",style=MaterialTheme.typography.titleMedium)
  OutlinedCard(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.outlinedCardColors(containerColor=MaterialTheme.colorScheme.surface)) { Column(Modifier.fillMaxWidth().padding(16.dp),verticalArrangement=Arrangement.spacedBy(20.dp)) {
   listOf("Abiertos" to resumen.abiertos,"En proceso" to resumen.enProceso,"Resueltos" to resumen.resueltos,"Cerrados" to resumen.cerrados).chunked(2).forEach { fila ->
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(16.dp)) {
     fila.forEach { (t,n) -> Row(Modifier.weight(1f),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
      Box(Modifier.size(8.dp).background(when(t) { "Abiertos" -> Abierto;"En proceso" -> EnProceso;"Resueltos" -> Resuelto;else -> Cerrado },CircleShape))
      Column { Text("$n",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.SemiBold);Text(t,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
     } }
    }
   }
  } }
  Text("Preferencias",style=MaterialTheme.typography.titleMedium)
  OutlinedCard(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.outlinedCardColors(containerColor=MaterialTheme.colorScheme.surface)) {
   Column(Modifier.padding(horizontal=16.dp,vertical=4.dp)) {
    AjusteSwitch("Modo oscuro",oscuro,sesion::modoOscuro)
    HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant)
    AjusteSwitch("Notificaciones",notificaciones,sesion::notificaciones,
     "Guarda tu preferencia. Esta versión de práctica no envía avisos del sistema.")
   }
  }
  OutlinedCard(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.outlinedCardColors(containerColor=MaterialTheme.colorScheme.surface)) {
   Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
    Text("Modo práctica",style=MaterialTheme.typography.titleMedium)
    Text("Cambia de perfil para probar la atención de tus incidencias en este dispositivo.",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
    AyudaButton("Cambiar a Soporte TI",!ocupado,onCambiar)
   }
  }
  OutlinedButton(onClick=onSalir,modifier=Modifier.fillMaxWidth()) { Text("Cerrar sesión") }
  error?.let { Mensaje(it) }
 }
}
@Composable fun AjusteSwitch(texto: String, valor: Boolean, cambio: (Boolean) -> Unit, descripcion: String? = null) {
 Row(Modifier.fillMaxWidth().heightIn(min=56.dp).toggleable(value=valor,role=Role.Switch,onValueChange=cambio).padding(vertical=8.dp),horizontalArrangement=Arrangement.spacedBy(16.dp),verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
  Column(Modifier.weight(1f)) {
   Text(texto)
   descripcion?.let { Text(it,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
  }
  Switch(checked=valor,onCheckedChange=null)
 }
}
