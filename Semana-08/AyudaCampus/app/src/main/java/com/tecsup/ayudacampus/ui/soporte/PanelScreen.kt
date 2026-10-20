package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.PanelViewModel
import com.tecsup.ayudacampus.ui.components.graficos.*
import com.tecsup.ayudacampus.ui.theme.*
@Composable fun PanelScreen(vm: PanelViewModel, onVer: () -> Unit) {
 val a by vm.abiertos.collectAsState();val p by vm.enProceso.collectAsState();val r by vm.resueltos.collectAsState()
 val estados by vm.porEstado.collectAsState();val categorias by vm.porCategoria.collectAsState();val alertas by vm.altasSinAtender.collectAsState()
 val promedio by vm.promedioResolucion.collectAsState()
 LazyColumn(contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  item { Text("Panel de soporte",style=MaterialTheme.typography.headlineMedium);Text("Resumen de incidencias del campus") }
  item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
   Text("Tiempo promedio de resolución",style=MaterialTheme.typography.titleMedium)
   Text(promedio,style=MaterialTheme.typography.headlineSmall)
   Text("Desde la creación hasta el cierre por el estudiante",style=MaterialTheme.typography.bodySmall)
  } } }
  item { Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   listOf(Triple("Abiertos",a,Abierto),Triple("En proceso",p,EnProceso),Triple("Resueltos",r,Resuelto)).forEach { (titulo,n,color) ->
    Card(Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=color.copy(alpha=.12f))) { Column(Modifier.padding(12.dp)) { Text("$n",style=MaterialTheme.typography.headlineMedium,color=color);Text(titulo,style=MaterialTheme.typography.labelSmall) } }
   }
  } }
  if(alertas>0) item { Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)) { Column(Modifier.padding(16.dp)) { Text("$alertas tickets de prioridad alta sin atender",style=MaterialTheme.typography.titleSmall);Text("Abiertos hace más de una hora");TextButton(onClick=onVer) { Text("Ver") } } } }
  item { Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(16.dp)) { Text("Tickets por estado",style=MaterialTheme.typography.titleMedium);GraficoDona(estados) } } }
  item { Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) { Column(Modifier.padding(16.dp)) { Text("Tickets por categoría",style=MaterialTheme.typography.titleMedium);Spacer(Modifier.height(16.dp));GraficoBarras(categorias) } } }
  item { Button(onClick=onVer,modifier=Modifier.fillMaxWidth()) { Text("Ver bandeja de tickets") } }
 }
}
