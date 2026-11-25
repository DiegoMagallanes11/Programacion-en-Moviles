package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val resultado = estado) {
        UiState.Cargando -> Cargando("Cargando publicación…", modifier)
        is UiState.Error -> ErrorConReintento(
            resultado.mensaje,
            { vm.abrir(id) },
            modifier.padding(16.dp)
        )
        is UiState.Exito -> {
            val (post, comentarios) = resultado.datos
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Usuario ${post.userId}", color = MaterialTheme.colorScheme.primary)
                        Text(post.title, style = MaterialTheme.typography.headlineSmall)
                        Text(post.body)
                    }
                }
                item {
                    HorizontalDivider(Modifier.padding(bottom = 12.dp))
                    Text("Comentarios (${comentarios.size})", style = MaterialTheme.typography.titleLarge)
                }
                items(comentarios, key = { it.id }) { comentario ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(comentario.email, color = MaterialTheme.colorScheme.primary)
                        Text(comentario.body)
                    }
                }
            }
        }
    }
}
