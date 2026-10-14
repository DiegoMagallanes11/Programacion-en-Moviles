package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.ui.components.*
@OptIn(ExperimentalLayoutApi::class)
@Composable fun NuevoTicketScreen(vm: NuevoTicketViewModel, onEnviado: () -> Unit) {
 val f by vm.formulario.collectAsState();val errores by vm.errores.collectAsState();val categorias by vm.categorias.collectAsState()
 val enviado by vm.enviado.collectAsState();val enviando by vm.enviando.collectAsState();val abierta by vm.recibirTickets.collectAsState()
 LaunchedEffect(enviado) { if(enviado) onEnviado() }
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Nuevo ticket",style=MaterialTheme.typography.headlineMedium);Text("Cuéntanos qué sucede")
  if(!abierta) Mensaje("Mesa de ayuda cerrada")
  Text("Categoría",style=MaterialTheme.typography.titleSmall)
  FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   categorias.forEach { c -> FilterChip(selected=f.categoriaId==c.id,onClick={vm.formulario.value=f.copy(categoriaId=c.id)},label={Text(c.nombre)},leadingIcon={Icon(iconoCategoria(c.icono),null,Modifier.size(18.dp))}) }
  }
  errores["categoria"]?.let { Mensaje(it) }
  AyudaTextField(f.titulo,{vm.formulario.value=f.copy(titulo=it)},"Título",errores["titulo"])
  AyudaTextField(f.ubicacion,{vm.formulario.value=f.copy(ubicacion=it)},"Ubicación")
  Text("Prioridad",style=MaterialTheme.typography.titleSmall)
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Prioridad.entries.forEach { p -> FilterChip(selected=f.prioridad==p,onClick={vm.formulario.value=f.copy(prioridad=p)},label={Text(p.name.lowercase().replaceFirstChar{it.uppercase()})}) } }
  AyudaTextField(f.descripcion,{vm.formulario.value=f.copy(descripcion=it)},"Descripción",errores["descripcion"],multilinea=true)
  errores["general"]?.let { Mensaje(it) }
  AyudaButton(if(enviando) "Enviando…" else "Enviar ticket",abierta && !enviando) { vm.enviar() }
 }
}
