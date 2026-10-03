package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Pantalla 3: Inicio / Productos (mockup "Cliente").
 * Muestra el catálogo en una LazyColumn y permite filtrarlo
 * por categoría con una fila horizontal de chips (LazyRow).
 */
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    categoriaInicial: String = "Todos",
    onProductoClick: (Producto) -> Unit = {},
    onAgregarProducto: (Producto) -> Unit = {},
    onCategoriaCambiada: (String) -> Unit = {}
) {
    var categoriaSeleccionada by remember(categoriaInicial) { mutableStateOf(categoriaInicial) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Consulta normalizada: sin espacios sobrantes, minúsculas y sin tildes,
    // para que "limon" encuentre "Limón" mientras el usuario escribe.
    val consultaBusqueda = remember(textoBusqueda) { normalizar(textoBusqueda) }

    // El filtro depende del estado: al escribir o cambiar el chip se recompone la lista.
    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda = consultaBusqueda.isEmpty() ||
            producto.nombre.normalizadoContiene(consultaBusqueda) ||
            producto.descripcion.normalizadoContiene(consultaBusqueda) ||
            producto.categoria.normalizadoContiene(consultaBusqueda)
        coincideCategoria && coincideBusqueda
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
            label = { Text("Buscar") },
            placeholder = { Text("Buscar productos...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
                text = if (categoriaSeleccionada == "Todos") "Productos destacados"
                else "Categoría: $categoriaSeleccionada",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(listaCategorias) { categoria ->
                    ChipCategoria(
                        texto = categoria,
                        seleccionado = categoria == categoriaSeleccionada,
                        onClick = {
                            categoriaSeleccionada = categoria
                            onCategoriaCambiada(categoria)
                        }
                    )
                }
            }

            if (productosFiltrados.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sin resultados",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "No encontramos productos con ese nombre.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = { onProductoClick(producto) },
                            onAgregar = { onAgregarProducto(producto) }
                        )
                    }
                }
            }
        }
    }
}

// Sub-composables PRIVADOS: solo los usa esta pantalla.

/**
 * Quita tildes, pasa a minúsculas y recorta espacios para comparar textos.
 * Así "LIMÓN", "limon " y "limón" se consideran la misma búsqueda.
 */
private fun normalizar(texto: String): String =
    java.text.Normalizer
        .normalize(texto, java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
        .trim()

private fun String.normalizadoContiene(consulta: String): Boolean =
    normalizar(this).contains(consulta)

@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurface

    Column(
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
        InicioScreen()
    }
}