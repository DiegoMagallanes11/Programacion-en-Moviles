package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

data class CategoriaItem(
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector
)

/**
 * Pantalla de Categorías.
 * Muestra tarjetas con las categorías disponibles y redirige al Inicio
 * filtrando por la categoría seleccionada al hacer clic.
 */
@Composable
fun CategoriasScreen(
    productos: List<Producto> = listaProductosFake,
    onSeleccionarCategoria: (String) -> Unit
) {
    val categoriasInfo = listOf(
        CategoriaItem(
            nombre = "Todos",
            descripcion = "Todos los productos disponibles",
            icono = Icons.Default.GridView
        ),
        CategoriaItem(
            nombre = "Abarrotes",
            descripcion = "Arroz, aceite, leche, menestras y básicos",
            icono = Icons.Default.ShoppingBag
        ),
        CategoriaItem(
            nombre = "Bebidas",
            descripcion = "Gaseosas, jugos, agua y refrescos",
            icono = Icons.Default.LocalDrink
        ),
        CategoriaItem(
            nombre = "Snacks",
            descripcion = "Galletas, piqueos, chocolates y dulces",
            icono = Icons.Default.Fastfood
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Categorías de Productos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecciona una categoría para ver sus productos",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(categoriasInfo) { item ->
                val cantidad = if (item.nombre == "Todos") {
                    productos.size
                } else {
                    productos.count { it.categoria == item.nombre }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarCategoria(item.nombre) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = VerdeBodega.copy(alpha = 0.12f))
                            ) {
                                Icon(
                                    imageVector = item.icono,
                                    contentDescription = item.nombre,
                                    tint = VerdeBodega,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(28.dp)
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = item.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = item.descripcion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "$cantidad ${if (cantidad == 1) "producto" else "productos"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VerdeBodega,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Ir a ${item.nombre}",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(onSeleccionarCategoria = {})
    }
}
