package com.tecsup.mibodega.ui.cliente.screens.entrega

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
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

private enum class TipoEntrega(val titulo: String, val costo: Double) {
    DELIVERY("Delivery (+ S/ 5.00)", 5.00),
    RECOJO("Recojo en Tienda (S/ 0.00)", 0.00)
}

/**
 * Pantalla 6: Datos de entrega (mockup "Cliente").
 * Recoge nombre, teléfono, tipo de entrega (Delivery o Recojo en tienda), dirección y referencia.
 *
 * Suma dinámicamente el costo de envío al total a pagar.
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
    var tipoEntrega by remember { mutableStateOf(TipoEntrega.DELIVERY) }

    var intentoEnvio by remember { mutableStateOf(false) }

    val esDelivery = tipoEntrega == TipoEntrega.DELIVERY
    val costoEnvio = tipoEntrega.costo
    val totalGeneral = subtotal + costoEnvio

    val nombreError = intentoEnvio && nombre.isBlank()
    val telefonoError = intentoEnvio && telefono.isBlank()
    val direccionError = intentoEnvio && esDelivery && direccion.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(20.dp))

        // Selección de Tipo de Entrega con RadioButtons
        Text(
            text = "Método de entrega",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                TipoEntrega.entries.forEach { opcion ->
                    val seleccionado = (tipoEntrega == opcion)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = seleccionado,
                                onClick = { tipoEntrega = opcion },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = seleccionado,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = opcion.titulo,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez",
            isError = nombreError
        )
        if (nombreError) {
            Text(
                text = "El nombre es obligatorio",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            isError = telefonoError
        )
        if (telefonoError) {
            Text(
                text = "El teléfono es obligatorio",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        if (esDelivery) {
            CampoTexto(
                etiqueta = "Dirección de entrega",
                valor = direccion,
                onValorCambia = { direccion = it },
                placeholder = "Av. Los Olivos 123",
                isError = direccionError
            )
            if (direccionError) {
                Text(
                    text = "La dirección es obligatoria para delivery",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            CampoTexto(
                etiqueta = "Referencia",
                valor = referencia,
                onValorCambia = { referencia = it },
                placeholder = "Frente al parque"
            )
            Spacer(Modifier.height(20.dp))
        }

        // Resumen del total
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
                        text = if (costoEnvio == 0.0) "Gratis (Recojo en tienda)" else "S/ %.2f".format(costoEnvio),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (costoEnvio == 0.0) VerdeBodega else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total general a pagar:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "S/ %.2f".format(totalGeneral),
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
            onClick = {
                intentoEnvio = true
                val direccionFinal = if (esDelivery) direccion else "Recojo en Tienda"
                val direccionValida = !esDelivery || direccion.isNotBlank()

                if (nombre.isNotBlank() && telefono.isNotBlank() && direccionValida) {
                    onConfirmarEntrega(nombre, telefono, direccionFinal, referencia, totalGeneral)
                }
            }
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
        IconButton(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
