package com.tecsup.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.tecsup.ayudacampus.model.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.ui.theme.*
@OptIn(ExperimentalLayoutApi::class)
@Composable fun TicketCard(detalle: TicketConDetalle, mostrarCliente: Boolean = false, sla: Sla, onClick: () -> Unit) {
 val t=detalle.ticket
 Card(onClick=onClick,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),
  border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
  colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
  Row(Modifier.height(IntrinsicSize.Min)) {
   Box(Modifier.width(5.dp).fillMaxHeight().background(when(t.prioridad) { "ALTA" -> Alta; "MEDIA" -> Media; else -> Cerrado }))
   Column(Modifier.weight(1f).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
     Surface(shape=RoundedCornerShape(12.dp),color=MaterialTheme.colorScheme.primaryContainer) {
      Icon(iconoCategoria(detalle.categoria.icono),null,Modifier.padding(10.dp).size(20.dp),tint=MaterialTheme.colorScheme.primary)
     }
     Text("#${t.id.toString().padStart(4,'0')} · ${detalle.categoria.nombre}",Modifier.weight(1f),style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
     Icon(Icons.Default.ChevronRight,null,Modifier.size(20.dp),tint=MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Text(t.titulo,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold)
    if(mostrarCliente) Text(detalle.cliente.nombre,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) { EstadoChip(t.estado);PrioridadChip(t.prioridad) }
    Surface(shape=RoundedCornerShape(10.dp),color=when(sla.situacion) {
     SituacionSla.VENCIDO -> MaterialTheme.colorScheme.errorContainer
     SituacionSla.ACTIVO -> MaterialTheme.colorScheme.primaryContainer
     SituacionSla.FINALIZADO -> MaterialTheme.colorScheme.surfaceContainerLow
    }) { Box(Modifier.padding(horizontal=10.dp,vertical=8.dp)) { SlaTexto(sla) } }
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(6.dp)) {
     Icon(Icons.Default.LocationOn,null,Modifier.size(16.dp),tint=MaterialTheme.colorScheme.onSurfaceVariant)
     Text("${t.ubicacion.ifBlank { "Sin ubicación" }} · "+java.text.SimpleDateFormat("dd/MM HH:mm",java.util.Locale.getDefault()).format(java.util.Date(t.fecha)),style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
   }
  }
 }
}
@Composable fun FiltrosEstado(actual: String, incluirCerrados: Boolean = false, onCambio: (String) -> Unit) {
 androidx.compose.foundation.lazy.LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
  items(if(incluirCerrados) 5 else 4) { i ->
   val estados=listOf("","ABIERTO","EN_PROCESO","RESUELTO","CERRADO")
   val etiquetas=listOf("Todos","Abiertos","En proceso","Resueltos","Cerrados")
   FilterChip(selected=actual==estados[i],onClick={onCambio(estados[i])},label={Text(etiquetas[i])})
  }
 }
}
