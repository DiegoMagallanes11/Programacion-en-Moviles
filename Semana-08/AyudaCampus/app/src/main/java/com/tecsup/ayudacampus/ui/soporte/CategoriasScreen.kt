package com.tecsup.ayudacampus.ui.soporte
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.ui.components.iconoCategoria
@Composable fun CategoriasScreen(vm: CategoriasViewModel, onEditar: (Int) -> Unit = {}, onNueva: () -> Unit = {}) {
 val categorias by vm.categorias.collectAsState();val error by vm.error.collectAsState()
 LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(20.dp)) {
  item { Text("Categorías",style=MaterialTheme.typography.headlineMedium); Text("Organiza las incidencias del campus"); Button(onClick=onNueva) { Text("Nueva categoría") } }
  if(error!=null) item { Text(error!!,color=MaterialTheme.colorScheme.error) }
  items(categorias,key={it.id}) { c ->
   Card(onClick={onEditar(c.id)},modifier=Modifier.fillMaxWidth()) {
    Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
     Icon(iconoCategoria(c.icono),null); Column(Modifier.weight(1f)) { Text(c.nombre);Text(if(c.activa) "Activa" else "Inactiva",style=MaterialTheme.typography.labelSmall) };Switch(checked=c.activa,onCheckedChange={vm.alternar(c)})
    }
   }
  }
 }
}
