package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ayudacampus.model.Prioridad
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import com.tecsup.ayudacampus.ui.components.*

@OptIn(ExperimentalLayoutApi::class)
@Composable fun BandejaScreen(vm: BandejaViewModel, onAtender: (Int) -> Unit) {
 val pantalla by vm.pantalla.collectAsStateWithLifecycle()
 val filtro = pantalla.filtros
 var categoriasAbiertas by remember { mutableStateOf(false) }
 LazyColumn(Modifier.fillMaxSize().imePadding(),contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  item { Text("Bandeja de tickets",style=MaterialTheme.typography.headlineMedium);Text("Ordenados por prioridad y fecha") }
  item { AyudaTextField(filtro.texto,vm::buscar,"Buscar por título, descripción o número") }
  item { FiltrosEstado(filtro.estado,incluirCerrados=true,onCambio=vm::cambiarEstado) }
  item {
   Text("Prioridad",style=MaterialTheme.typography.labelLarge)
   FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
    FilterChip(selected=filtro.prioridad==null,onClick={vm.cambiarPrioridad(null)},label={Text("Todas")})
    listOf(Prioridad.ALTA,Prioridad.MEDIA,Prioridad.BAJA).forEach { p -> FilterChip(selected=filtro.prioridad==p,onClick={vm.cambiarPrioridad(p)},label={Text(p.name)}) }
   }
  }
  item { Box {
   OutlinedButton(onClick={categoriasAbiertas=true},modifier=Modifier.fillMaxWidth()) {
    Text("Categoría: "+(pantalla.categorias.firstOrNull { it.id==filtro.categoriaId }?.nombre ?: "Todas"))
   }
   DropdownMenu(expanded=categoriasAbiertas,onDismissRequest={categoriasAbiertas=false}) {
    DropdownMenuItem(text={Text("Todas las categorías")},onClick={vm.cambiarCategoria(null);categoriasAbiertas=false})
    pantalla.categorias.forEach { c -> DropdownMenuItem(
     text={Text(c.nombre+if(c.activa) "" else " (inactiva)")},
     onClick={vm.cambiarCategoria(c.id);categoriasAbiertas=false}) }
   }
  } }
  item {
   if (!pantalla.cargando) Text("${pantalla.tarjetas.size} resultado${if(pantalla.tarjetas.size==1) "" else "s"}",style=MaterialTheme.typography.titleSmall)
   TextButton(onClick=vm::limpiarFiltros) { Text("Limpiar filtros") }
  }
  if (pantalla.cargando) item { CircularProgressIndicator() }
  else if (pantalla.tarjetas.isEmpty()) item {
   Text(if(pantalla.total==0) "La bandeja está vacía" else "No hay tickets que coincidan con los filtros")
  }
  items(pantalla.tarjetas,key={it.detalle.ticket.id}) { item ->
   TicketCard(item.detalle,true,item.sla) { onAtender(item.detalle.ticket.id) }
  }
 }
}
