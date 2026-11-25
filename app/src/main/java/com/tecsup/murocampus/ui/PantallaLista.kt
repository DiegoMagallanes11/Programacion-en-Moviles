package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, abrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = vm::buscar,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
            label = { Text("Buscar por título") }
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") }
                )
            }
            items((1..10).toList()) { usuario ->
                FilterChip(
                    selected = filtro == usuario,
                    onClick = { vm.filtrar(usuario) },
                    label = { Text("Usuario $usuario") }
                )
            }
        }

        when (val resultado = estado) {
            UiState.Cargando -> Cargando("Cargando publicaciones…", Modifier.weight(1f))
            is UiState.Error -> ErrorConReintento(
                resultado.mensaje,
                vm::cargar,
                Modifier.padding(16.dp)
            )
            is UiState.Exito -> {
                if (resultado.datos.isEmpty()) {
                    val mensaje = if (busqueda.isBlank()) "Sin publicaciones" else "Sin resultados"
                    Text(mensaje, Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(resultado.datos, key = { it.id }) { post ->
                            TarjetaPost(post) { abrir(post.id) }
                        }
                    }
                }
            }
        }
    }
}
