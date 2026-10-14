package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import com.tecsup.ayudacampus.ui.components.*
@Composable fun BandejaScreen(vm: BandejaViewModel, onAtender: (Int) -> Unit) {
 val tickets by vm.tickets.collectAsState();val estado by vm.estado.collectAsState()
 LazyColumn(contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
  item { Text("Bandeja de tickets",style=MaterialTheme.typography.headlineMedium);Text("Ordenados por prioridad y fecha") }
  item { FiltrosEstado(estado) { vm.estado.value=it } }
  if(tickets.isEmpty()) item { Text("No hay tickets aquí") }
  items(tickets,key={it.ticket.id}) { TicketCard(it,true) { onAtender(it.ticket.id) } }
 }
}
