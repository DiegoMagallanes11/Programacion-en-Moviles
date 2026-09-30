package com.fernandez.lab06magallanes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf("Inicio") }

    // Lista mutable de productos para cambiar el estado de favorito
    val listaProductos = remember {
        mutableStateListOf(
            Producto(1, "Audifonos", 89.00),
            Producto(2, "Smartwatch", 199.00),
            Producto(3, "Funda celular", 25.00)
        )
    }

    // Cuenta cuantos productos estan marcados como favorito
    val totalFavoritos = listaProductos.count { it.esFavorito }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                opcionSeleccionada = pantallaActual,
                cantidadFavoritos = totalFavoritos,
                onOpcionSeleccionada = { opcion ->
                    pantallaActual = opcion
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (pantallaActual) {
                    "Inicio" -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(listaProductos) { prod ->
                                TarjetaProducto(
                                    producto = prod,
                                    onToggleFavorito = {
                                        val index = listaProductos.indexOf(prod)
                                        if (index != -1) {
                                            listaProductos[index] = prod.copy(esFavorito = !prod.esFavorito)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    "Favoritos" -> {
                        val favoritos = listaProductos.filter { it.esFavorito }
                        if (favoritos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No tienes favoritos agregados")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                items(favoritos) { prod ->
                                    TarjetaProducto(
                                        producto = prod,
                                        onToggleFavorito = {
                                            val index = listaProductos.indexOf(prod)
                                            if (index != -1) {
                                                listaProductos[index] = prod.copy(esFavorito = !prod.esFavorito)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Pantalla: $pantallaActual")
                        }
                    }
                }
            }
        }
    }
}