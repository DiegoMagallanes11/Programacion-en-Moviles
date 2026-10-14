package com.tecsup.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecsup.ayudacampus.ui.theme.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun AppTopBar(titulo: String, volver: (() -> Unit)? = null) {
 TopAppBar(title={Text(titulo)}, navigationIcon={ if(volver != null) IconButton(onClick=volver) { Icon(Icons.Default.ArrowBack,"Volver") } })
}
@Composable fun AyudaButton(texto: String, enabled: Boolean = true, onClick: () -> Unit) {
 Button(onClick=onClick,enabled=enabled,modifier=Modifier.fillMaxWidth().heightIn(min=50.dp),shape=RoundedCornerShape(12.dp)) { Text(texto) }
}
@Composable fun AyudaTextField(valor: String, cambio: (String) -> Unit, etiqueta: String, error: String? = null, secreto: Boolean = false, multilinea: Boolean = false) {
 OutlinedTextField(value=valor,onValueChange=cambio,label={Text(etiqueta)},modifier=Modifier.fillMaxWidth(),isError=error != null,
  supportingText={if(error != null) Text(error)},singleLine=!multilinea,minLines=if(multilinea) 3 else 1,
  visualTransformation=if(secreto) PasswordVisualTransformation() else VisualTransformation.None,
  keyboardOptions=androidx.compose.foundation.text.KeyboardOptions(keyboardType=if(secreto) KeyboardType.Password else KeyboardType.Text),shape=RoundedCornerShape(12.dp))
}
fun iconoCategoria(nombre: String): ImageVector = when(nombre) {
 "pc" -> Icons.Default.Computer; "code" -> Icons.Default.Code; "wifi" -> Icons.Default.Wifi
 "key" -> Icons.Default.Key; "printer" -> Icons.Default.Print; else -> Icons.Default.Label
}
fun etiquetaEstado(estado: String) = when(estado) { "ABIERTO" -> "Abierto"; "EN_PROCESO" -> "En proceso"; "RESUELTO" -> "Resuelto"; else -> "Cerrado" }
@Composable fun EstadoChip(estado: String) {
 val color = when(estado) { "ABIERTO" -> Abierto; "EN_PROCESO" -> EnProceso; "RESUELTO" -> Resuelto; else -> Cerrado }
 Surface(color=color.copy(alpha=.12f),shape=RoundedCornerShape(8.dp)) { Text(etiquetaEstado(estado),color=color,modifier=Modifier.padding(horizontal=8.dp,vertical=4.dp),style=MaterialTheme.typography.labelMedium) }
}
@Composable fun PrioridadChip(prioridad: String) {
 val color=when(prioridad) { "ALTA" -> Alta; "MEDIA" -> Media; else -> Cerrado }
 Surface(color=color.copy(alpha=.12f),shape=RoundedCornerShape(8.dp)) { Text(prioridad.lowercase().replaceFirstChar{it.uppercase()},color=color,modifier=Modifier.padding(8.dp,4.dp),style=MaterialTheme.typography.labelMedium) }
}
@Composable fun Estrellas(valor: Int, onCambio: ((Int) -> Unit)? = null) {
 Row { (1..5).forEach { n -> IconButton(onClick={onCambio?.invoke(n)},enabled=onCambio != null) {
  Icon(if(n<=valor) Icons.Default.Star else Icons.Default.StarBorder, "Calificar $n estrellas",tint=if(n<=valor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
 } } }
}
@Composable fun StepperTicket(estado: String) {
 val estados=listOf("ABIERTO","EN_PROCESO","RESUELTO","CERRADO")
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
  estados.forEachIndexed { i,e -> Column(horizontalAlignment=Alignment.CenterHorizontally) {
   Icon(if(i<=estados.indexOf(estado)) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null,tint=if(i<=estados.indexOf(estado)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
   Text(etiquetaEstado(e),style=MaterialTheme.typography.labelSmall)
  } }
 }
}
@Composable fun BurbujaComentario(nombre: String, texto: String, propio: Boolean, fecha: Long) {
 Column(Modifier.fillMaxWidth(),horizontalAlignment=if(propio) Alignment.End else Alignment.Start) {
  Surface(color=if(propio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,shape=RoundedCornerShape(12.dp),modifier=Modifier.fillMaxWidth(.88f)) {
   Column(Modifier.padding(12.dp)) { Text(nombre,style=MaterialTheme.typography.labelMedium);Text(texto); Text(java.text.SimpleDateFormat("dd/MM HH:mm",java.util.Locale.getDefault()).format(java.util.Date(fecha)),style=MaterialTheme.typography.labelSmall) }
  }
 }
}
@Composable fun Mensaje(texto: String) { Text(texto,color=MaterialTheme.colorScheme.error,modifier=Modifier.padding(vertical=8.dp)) }
