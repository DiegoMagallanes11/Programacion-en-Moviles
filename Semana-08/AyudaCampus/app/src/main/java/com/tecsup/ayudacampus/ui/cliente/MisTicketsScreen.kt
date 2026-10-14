package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.ui.components.*
@Composable fun MisTicketsScreen(vm: MisTicketsViewModel, nombre: String, onDetalle: (Int) -> Unit, onNuevo: () -> Unit) {
 val tickets by vm.tickets.collectAsState();val abiertos by vm.abiertos.collectAsState();val estado by vm.estado.collectAsState();val texto by vm.busqueda.collectAsState()
 Box(Modifier.fillMaxSize()) {
  LazyColumn(contentPadding=PaddingValues(20.dp,20.dp,20.dp,96.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
   item { Text("Hola, ${nombre.substringBefore(' ')}",style=MaterialTheme.typography.headlineMedium);Text("Tienes $abiertos tickets abiertos",color=MaterialTheme.colorScheme.onSurfaceVariant) }
   item { AyudaTextField(texto,{vm.busqueda.value=it},"Buscar por título o número") }
   item { FiltrosEstado(estado) { vm.estado.value=it } }
   if(tickets.isEmpty()) item { Text("No tienes tickets aquí",modifier=Modifier.padding(vertical=24.dp)) }
   items(tickets,key={it.ticket.id}) { TicketCard(it,onClick={onDetalle(it.ticket.id)}) }
  }
  FloatingActionButton(onClick=onNuevo,modifier=Modifier.align(androidx.compose.ui.Alignment.BottomEnd).padding(20.dp)) { Icon(Icons.Default.Add,"Crear ticket") }
 }
}
