package com.tecsup.ayudacampus.ui.components.graficos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.ui.theme.*
import com.tecsup.ayudacampus.model.DatoGrafico
@Composable fun GraficoDona(datos: List<DatoGrafico>) {
 val colores=listOf(Abierto,EnProceso,Resuelto,Cerrado)
 val total=datos.sumOf { it.valor };val fondo=MaterialTheme.colorScheme.surfaceVariant
 Canvas(Modifier.fillMaxWidth().height(180.dp).semantics { contentDescription=datos.joinToString { "${it.etiqueta}: ${it.valor}" } }) {
  val lado=size.minDimension-28.dp.toPx();val inicio=Offset((size.width-lado)/2,(size.height-lado)/2);val stroke=Stroke(22.dp.toPx())
  drawArc(fondo,0f,360f,false,inicio,Size(lado,lado),style=stroke)
  var angulo=-90f
  if(total>0) datos.forEachIndexed { i,d -> val tramo=d.valor*360f/total;drawArc(colores[i%colores.size],angulo,tramo,false,inicio,Size(lado,lado),style=stroke);angulo+=tramo }
 }
 datos.forEachIndexed { i,d -> Text("● ${d.etiqueta}: ${d.valor}",color=colores[i%colores.size],style=MaterialTheme.typography.bodySmall) }
 if(total==0) Text("Sin tickets para mostrar")
}
@Composable fun GraficoBarras(datos: List<DatoGrafico>) {
 val color=MaterialTheme.colorScheme.primary;val fondo=MaterialTheme.colorScheme.surfaceVariant
 val max=datos.maxOfOrNull { it.valor }?.coerceAtLeast(1) ?: 1
 Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
  datos.forEach { d ->
   Text("${d.etiqueta} · ${d.valor}",style=MaterialTheme.typography.labelMedium)
   Canvas(Modifier.fillMaxWidth().height(14.dp).semantics { contentDescription="${d.etiqueta}: ${d.valor}" }) {
    drawRoundRect(fondo,cornerRadius=CornerRadius(8f));if(d.valor>0) drawRoundRect(color,size=Size(size.width*d.valor/max,size.height),cornerRadius=CornerRadius(8f))
   }
  }
 }
}
