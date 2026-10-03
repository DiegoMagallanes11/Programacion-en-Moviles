package com.tecsup.mibodega.ui.cliente.screens.pedidos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro

/**
 * Pantalla "Mis Pedidos" (mockup "Cliente").
 * Por ahora solo muestra el estado vacío: el historial de pedidos
 * se irá llenando a medida que el usuario confirme compras.
 */
@Composable
fun PedidosScreen() {
    Scaffold { paddingInterno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = GrisClaro,
                    modifier = Modifier.size(96.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Aún no tienes pedidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Cuando confirmes tu primer pedido,\naparecerá aquí el historial.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidosPreview() {
    BodegaTheme {
        PedidosScreen()
    }
}