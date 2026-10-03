package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla de Categorías (mockup "Cliente").
 * Lista las categorías disponibles y avisa cuál se seleccionó
 * para que ClienteApp lleve al Inicio ya filtrado.
 */
@Composable
fun CategoriasScreen(
    onSeleccionarCategoria: (String) -> Unit
) {
    Scaffold { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaCategorias) { categoria ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GrisClaro, RoundedCornerShape(12.dp))
                            .clickable { onSeleccionarCategoria(categoria) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = categoria,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (categoria == "Todos") VerdeBodega
                            else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriasPreview() {
    BodegaTheme {
        CategoriasScreen(onSeleccionarCategoria = {})
    }
}