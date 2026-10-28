package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.*
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
@Composable fun LoginScreen(vm: SesionViewModel) {
 var correo by rememberSaveable { mutableStateOf("") };var clave by rememberSaveable { mutableStateOf("") }
 var admin by rememberSaveable { mutableStateOf(false) }
 val error by vm.error.collectAsState();val ocupado by vm.ocupado.collectAsState();val oscuro by vm.modoOscuro.collectAsState()
 AyudaTheme(if(admin) Rol.ADMIN else Rol.CLIENTE,oscuro) { Surface(Modifier.fillMaxSize(),color=MaterialTheme.colorScheme.background) {
  Box(Modifier.fillMaxSize().safeDrawingPadding().imePadding(),contentAlignment=Alignment.Center) {
  Column(Modifier.widthIn(max=520.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(28.dp),verticalArrangement=Arrangement.spacedBy(18.dp)) {
   Icon(Icons.Default.School,null,tint=MaterialTheme.colorScheme.primary,modifier=Modifier.size(56.dp))
   Text("Bienvenido",style=MaterialTheme.typography.displaySmall)
   Text("Ingresa para continuar",style=MaterialTheme.typography.bodyLarge,color=MaterialTheme.colorScheme.onSurfaceVariant)
   SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
    listOf("Estudiante","Soporte TI").forEachIndexed { i,t -> SegmentedButton(selected=admin==(i==1),onClick={admin=i==1;vm.limpiarError()},shape=SegmentedButtonDefaults.itemShape(i,2),modifier=Modifier.height(52.dp)) { Text(t) } }
   }
   AyudaTextField(correo,{correo=it;vm.limpiarError()},"Correo")
   AyudaTextField(clave,{clave=it;vm.limpiarError()},"Contraseña",secreto=true)
   error?.let { Mensaje(it) }
   AyudaButton(if(ocupado) "Ingresando…" else "Ingresar",!ocupado) { vm.login(correo,clave,if(admin) Rol.ADMIN else Rol.CLIENTE) }
  }
  }
 } }
}
