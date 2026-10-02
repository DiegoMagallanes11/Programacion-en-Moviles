package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Opciones de ordenamiento por precio.
 */
private enum class OrdenPrecio { NINGUNO, MENOR_A_MAYOR, MAYOR_A_MENOR }

/**
 * Pantalla 3: Inicio / Productos (mockup "Cliente").
 * La más completa: Scaffold (topBar + bottomBar), LazyRow de categorías
 * y LazyVerticalGrid de productos.
 *
 * Incluye:
 * - Filtro por categorías
 * - Búsqueda por nombre
 * - Filtro de favoritos (chip "Favoritos")
 * - Selector / menú de ordenamiento por precio (Menor a Mayor / Mayor a Menor)
 *
 * @param productos lista completa (fake por ahora, luego vendrá de un ViewModel)
 * @param cantidadCarrito para el badge del carrito en la topBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf(listaCategorias.first()) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var ordenPrecio by remember { mutableStateOf(OrdenPrecio.NINGUNO) }
    var menuOrdenExpandido by remember { mutableStateOf(false) }

    // Estado local de favoritos (set de IDs de productos marcados)
    var favoritosIds by remember(productos) {
        mutableStateOf(productos.filter { it.esFavorito }.map { it.id }.toSet())
    }

    val productosFiltrados = productos
        .filter { producto ->
            val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
            val coincideBusqueda = producto.nombre.contains(textoBusqueda, ignoreCase = true)
            val coincideFavorito = !soloFavoritos || favoritosIds.contains(producto.id)
            coincideCategoria && coincideBusqueda && coincideFavorito
        }
        .let { lista ->
            when (ordenPrecio) {
                OrdenPrecio.MENOR_A_MAYOR -> lista.sortedBy { it.precio }
                OrdenPrecio.MAYOR_A_MENOR -> lista.sortedByDescending { it.precio }
                OrdenPrecio.NINGUNO -> lista
            }
        }

    Scaffold { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GrisClaro,
                    focusedContainerColor = GrisClaro,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = VerdeBodega
                )
            )

            Text(
                text = "Productos destacados",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
            )

            // Fila de categorías + chip de Favoritos
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listaCategorias) { categoria ->
                    ChipCategoria(
                        texto = categoria,
                        seleccionado = categoria == categoriaSeleccionada,
                        onClick = { categoriaSeleccionada = categoria }
                    )
                }
                item {
                    FilterChip(
                        selected = soloFavoritos,
                        onClick = { soloFavoritos = !soloFavoritos },
                        label = { Text("Favoritos", fontWeight = FontWeight.Medium) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = if (soloFavoritos) Color.White else Color.Red,
                                modifier = Modifier.padding(0.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Red,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Selector / Menú desplegable de ordenamiento por precio
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${productosFiltrados.size} productos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box {
                    Row(
                        modifier = Modifier
                            .background(GrisClaro, RoundedCornerShape(12.dp))
                            .clickable { menuOrdenExpandido = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Ordenar por",
                            tint = VerdeBodega
                        )
                        Text(
                            text = when (ordenPrecio) {
                                OrdenPrecio.NINGUNO -> "Ordenar por precio"
                                OrdenPrecio.MENOR_A_MAYOR -> "Menor a Mayor"
                                OrdenPrecio.MAYOR_A_MENOR -> "Mayor a Menor"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = menuOrdenExpandido,
                        onDismissRequest = { menuOrdenExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin orden") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.NINGUNO
                                menuOrdenExpandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Menor a Mayor") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.MENOR_A_MAYOR
                                menuOrdenExpandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mayor a Menor") },
                            onClick = {
                                ordenPrecio = OrdenPrecio.MAYOR_A_MENOR
                                menuOrdenExpandido = false
                            }
                        )
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto) },
                        onAgregar = { onAgregarProducto(producto) },
                        esFavorito = favoritosIds.contains(producto.id),
                        onToggleFavorito = {
                            favoritosIds = if (favoritosIds.contains(producto.id)) {
                                favoritosIds - producto.id
                            } else {
                                favoritosIds + producto.id
                            }
                        }
                    )
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text = texto, color = contenido, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {}
        )
    }
}
