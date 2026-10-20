package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel
import com.tecsup.ayudacampus.model.*
import com.tecsup.ayudacampus.ui.components.*
@OptIn(ExperimentalLayoutApi::class)
@Composable fun AtenderTicketScreen(vm: AtenderViewModel, usuarioId: Int) {
 val sla by vm.sla.collectAsStateWithLifecycle()
 val d by vm.ticket.collectAsState();val elegido by vm.estadoElegido.collectAsState();val prioridad by vm.prioridadElegida.collectAsState()
 val error by vm.error.collectAsState();val mensaje by vm.mensaje.collectAsState();val ocupado by vm.ocupado.collectAsState()
 var texto by rememberSaveable { mutableStateOf("") }
 if(d==null) { Text("Cargando ticket…",Modifier.padding(24.dp));return }
 val detalle=d!!;val t=detalle.ticket;val cerrado=t.estado=="CERRADO"
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Atender #${t.id.toString().padStart(4,'0')}",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary)
  Text(t.titulo,style=MaterialTheme.typography.headlineSmall);Text("${detalle.cliente.nombre} · ${detalle.cliente.correo}")
  sla?.let { SlaTexto(it) }
  Text("${detalle.categoria.nombre} · ${t.ubicacion}");Text(t.descripcion)
  Text("Estado",style=MaterialTheme.typography.titleSmall)
  FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) { EstadoTicket.entries.forEach { e -> FilterChip(selected=(elegido?.name ?: t.estado)==e.name,onClick={vm.cambiarEstado(e)},enabled=!cerrado && e!=EstadoTicket.CERRADO && !ocupado,label={Text(etiquetaEstado(e.name))}) } }
  Text("Prioridad",style=MaterialTheme.typography.titleSmall)
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Prioridad.entries.forEach { p -> FilterChip(selected=(prioridad?.name ?: t.prioridad)==p.name,onClick={vm.cambiarPrioridad(p)},enabled=!cerrado && !ocupado,label={Text(p.name.lowercase().replaceFirstChar{it.uppercase()})}) } }
  AyudaButton("Guardar cambios",!cerrado && !ocupado) { vm.guardar() }
  mensaje?.let { Text(it,color=MaterialTheme.colorScheme.primary) };error?.let { Mensaje(it) }
  Text("Conversación",style=MaterialTheme.typography.titleMedium)
  detalle.comentarios.sortedWith(compareBy({it.fecha},{it.id})).forEach { c -> BurbujaComentario(c.autorNombre,c.texto,c.autorId==usuarioId,c.fecha) }
  if(!cerrado) { AyudaTextField(texto,{texto=it},"Escribe una respuesta",multilinea=true);AyudaButton("Enviar respuesta",texto.isNotBlank() && !ocupado) { vm.responder(texto);texto="" } }
  else { Text("El estudiante cerró este ticket");Estrellas(t.calificacion ?: 0) }
 }
}
