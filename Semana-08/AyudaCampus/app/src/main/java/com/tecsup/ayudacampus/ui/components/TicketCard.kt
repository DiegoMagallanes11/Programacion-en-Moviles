package com.tecsup.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.ui.theme.*
@Composable fun TicketCard(detalle: TicketConDetalle, mostrarCliente: Boolean = false, onClick: () -> Unit) {
 val t=detalle.ticket
 Card(onClick=onClick,modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
  Row(Modifier.height(IntrinsicSize.Min)) {
   Box(Modifier.width(5.dp).fillMaxHeight().background(when(t.prioridad) { "ALTA" -> Alta; "MEDIA" -> Media; else -> Cerrado }))
   Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Icon(iconoCategoria(detalle.categoria.icono),null,tint=MaterialTheme.colorScheme.primary); Text("#${t.id.toString().padStart(4,'0')} · ${detalle.categoria.nombre}",style=MaterialTheme.typography.labelMedium) }
    Text(t.titulo,style=MaterialTheme.typography.titleMedium)
    if(mostrarCliente) Text(detalle.cliente.nombre,style=MaterialTheme.typography.bodySmall)
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { EstadoChip(t.estado);PrioridadChip(t.prioridad) }
    Text("${t.ubicacion} · "+java.text.SimpleDateFormat("dd/MM HH:mm",java.util.Locale.getDefault()).format(java.util.Date(t.fecha)),style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
   }
  }
 }
}
@Composable fun FiltrosEstado(actual: String, onCambio: (String) -> Unit) {
 androidx.compose.foundation.lazy.LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
  items(4) { i ->
   val estados=listOf("","ABIERTO","EN_PROCESO","RESUELTO")
   val etiquetas=listOf("Todos","Abiertos","En proceso","Resueltos")
   FilterChip(selected=actual==estados[i],onClick={onCambio(estados[i])},label={Text(etiquetas[i])})
  }
 }
}
