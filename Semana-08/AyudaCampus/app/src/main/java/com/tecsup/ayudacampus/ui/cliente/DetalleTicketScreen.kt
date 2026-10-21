package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel
import com.tecsup.ayudacampus.ui.components.*
@Composable fun DetalleTicketScreen(vm: TicketDetalleViewModel, usuarioId: Int) {
 val sla by vm.sla.collectAsStateWithLifecycle()
 val d by vm.ticket.collectAsState();val error by vm.error.collectAsState();val ocupado by vm.ocupado.collectAsState()
 var texto by rememberSaveable { mutableStateOf("") };var final by rememberSaveable { mutableStateOf("") };var estrellas by rememberSaveable { mutableIntStateOf(0) }
 val enviado by vm.comentarioEnviado.collectAsState()
 LaunchedEffect(enviado) { enviado?.let { if(texto==it) texto="";vm.confirmarEnvioComentario() } }
 if(d==null) { Text("Cargando ticket o sin acceso…",Modifier.padding(24.dp));return }
 val detalle=d!!;val t=detalle.ticket
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding().padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("Ticket #${t.id.toString().padStart(4,'0')}",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary)
  Text(t.titulo,style=MaterialTheme.typography.headlineSmall)
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { EstadoChip(t.estado);PrioridadChip(t.prioridad) }
  sla?.let { SlaTexto(it) }
  Text("${detalle.categoria.nombre} · ${t.ubicacion}");Text(t.descripcion)
  StepperTicket(t.estado)
  Text("Conversación",style=MaterialTheme.typography.titleMedium)
  if(detalle.comentarios.isEmpty()) Text("Aún no hay comentarios")
  detalle.comentarios.sortedWith(compareBy({it.fecha},{it.id})).forEach { c -> BurbujaComentario(c.autorNombre,c.texto,c.autorId==usuarioId,c.fecha) }
  if(t.estado!="CERRADO") {
   AyudaTextField(texto,{texto=it},"Escribe un comentario",multilinea=true)
   AyudaButton("Enviar comentario",texto.isNotBlank() && !ocupado) { vm.comentar(texto) }
  }
  if(t.estado=="RESUELTO") {
   Card(colors=CardDefaults.cardColors(containerColor=com.tecsup.ayudacampus.ui.theme.Resuelto.copy(alpha=.12f))) {
    Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
     Text("Tu ticket ha sido resuelto",style=MaterialTheme.typography.titleMedium);Text("Califica la atención para confirmar el cierre.")
     Estrellas(estrellas) { estrellas=it }
     AyudaTextField(final,{final=it},"Comentario final (opcional)",multilinea=true)
     AyudaButton("Confirmar y cerrar",estrellas in 1..5 && !ocupado) { vm.cerrar(estrellas,final) }
     OutlinedButton(onClick={vm.reabrir()},enabled=!ocupado,modifier=Modifier.fillMaxWidth()) { Text("Reabrir ticket") }
    }
   }
  }
  if(t.estado=="CERRADO") { Text("Atención finalizada",style=MaterialTheme.typography.titleMedium);Estrellas(t.calificacion ?: 0) }
  error?.let { Mensaje(it) }
 }
}
