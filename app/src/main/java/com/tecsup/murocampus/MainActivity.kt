package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory
import com.tecsup.murocampus.viewmodel.UiState

class MainActivity : ComponentActivity() {
    private val factory by lazy { PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = factory)
                MuroCampus(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuroCampus(vm: PostsViewModel) {
    var seleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }
    val enLista = seleccionado == null && !creando
    val volver: () -> Unit = {
        if (creando) vm.cerrarCrear() else vm.cerrarDetalle()
        seleccionado = null
        creando = false
    }

    LaunchedEffect(seleccionado) {
        seleccionado?.let(vm::restaurarDetalle)
    }

    val borrado by vm.borrado.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var mostrarBorrado by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(borrado) {
        if (borrado is UiState.Exito && seleccionado != null) {
            mostrarBorrado = true
            volver()
        }
    }

    LaunchedEffect(mostrarBorrado) {
        if (mostrarBorrado) {
            snackbar.showSnackbar("Publicación eliminada de esta sesión")
            mostrarBorrado = false
        }
    }

    BackHandler(enabled = !enLista, onBack = volver)
    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text(if (creando) "Crear publicación" else "MuroCampus") },
                navigationIcon = {
                    if (!enLista) {
                        TextButton(onClick = volver) {
                            Text("Atrás", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (enLista) {
                FloatingActionButton(
                    onClick = { creando = true },
                    modifier = Modifier.semantics { contentDescription = "Crear publicación" }
                ) { Text("+", style = MaterialTheme.typography.headlineSmall) }
            }
        }
    ) { espacio ->
        val modifier = Modifier.padding(espacio)
        val id = seleccionado
        when {
            creando -> PantallaCrear(vm, modifier)
            id != null -> PantallaDetalle(vm, id, modifier)
            else -> PantallaLista(vm, abrir = { postId ->
                vm.abrir(postId)
                seleccionado = postId
            }, modifier = modifier)
        }
    }
}
