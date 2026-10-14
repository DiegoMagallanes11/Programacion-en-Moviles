package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.ui.components.*
@OptIn(ExperimentalLayoutApi::class)
@Composable fun FormularioCategoriaScreen(vm: CategoriasViewModel, id: Int, onTerminar: () -> Unit) {
 val c by vm.formulario.collectAsState();val error by vm.error.collectAsState();val guardado by vm.guardado.collectAsState();val ocupado by vm.ocupado.collectAsState()
 LaunchedEffect(id) { vm.cargar(id) };LaunchedEffect(guardado) { if(guardado) onTerminar() }
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text(if(id==0) "Nueva categoría" else "Editar categoría",style=MaterialTheme.typography.headlineMedium)
  AyudaTextField(c.nombre,{vm.formulario.value=c.copy(nombre=it)},"Nombre")
  Text("Icono")
  FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("pc","code","wifi","key","printer","tag").forEach { icono -> FilterChip(selected=c.icono==icono,onClick={vm.formulario.value=c.copy(icono=icono)},label={Icon(iconoCategoria(icono),icono)}) } }
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) { Text("Activa");Switch(c.activa,{vm.formulario.value=c.copy(activa=it)}) }
  Text("Vista previa",style=MaterialTheme.typography.titleSmall)
  Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(20.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) { Icon(iconoCategoria(c.icono),null);Text(c.nombre.ifBlank { "Nombre de categoría" }) } }
  error?.let { Mensaje(it) }
  AyudaButton("Guardar categoría",!ocupado) { vm.guardar(c) }
  if(id!=0) OutlinedButton(onClick={vm.eliminar(c)},enabled=!ocupado,modifier=Modifier.fillMaxWidth()) { Text("Eliminar categoría") }
 }
}
