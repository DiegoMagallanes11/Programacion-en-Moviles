package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.publicacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }
    val enviando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            texto = ""
        }
    }

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                vm.editarBorrador()
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título") },
            singleLine = true,
            enabled = !enviando
        )
        OutlinedTextField(
            value = texto,
            onValueChange = {
                texto = it
                vm.editarBorrador()
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contenido") },
            minLines = 4,
            enabled = !enviando
        )
        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = titulo.isNotBlank() && texto.isNotBlank() && !enviando
        ) { Text("Publicar") }

        when (val resultado = estado) {
            null -> Unit
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Text(
                "Publicado con id ${resultado.datos.id}",
                color = MaterialTheme.colorScheme.primary
            )
            is UiState.Error -> Text(resultado.mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
