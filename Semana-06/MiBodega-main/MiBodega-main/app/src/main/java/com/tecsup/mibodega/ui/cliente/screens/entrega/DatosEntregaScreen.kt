package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

private const val COSTO_DELIVERY = 4.00

/**
 * Pantalla 6: Datos de entrega (mockup "Cliente").
 * Recoge nombre, teléfono, dirección y referencia, y muestra el
 * resumen del total a pagar antes de confirmar.
 */
@Composable
fun DatosEntregaScreen(
    subtotal: Double = 0.0,
    onVolver: () -> Unit,
    onConfirmarEntrega: (nombre: String, telefono: String, direccion: String, referencia: String, total: Double) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    val totalGeneral = subtotal + COSTO_DELIVERY

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(20.dp))

        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123"
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia",
            valor = referencia,
            onValorCambia = { referencia = it },
            placeholder = "Frente al parque"
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GrisClaro)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (subtotal > 0.0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal productos:", style = MaterialTheme.typography.bodyMedium)
                        Text("S/ %.2f".format(subtotal), style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(4.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Costo de envío:", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "S/ %.2f".format(COSTO_DELIVERY),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total general a pagar:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "S/ %.2f".format(totalGeneral),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            onClick = { onConfirmarEntrega(nombre, telefono, direccion, referencia, totalGeneral) }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoEntrega(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Datos de entrega",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp))
    }
    Text(
        text = "Completa los datos para recibir tu pedido",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(subtotal = 25.00, onVolver = {}, onConfirmarEntrega = { _, _, _, _, _ -> })
    }
}