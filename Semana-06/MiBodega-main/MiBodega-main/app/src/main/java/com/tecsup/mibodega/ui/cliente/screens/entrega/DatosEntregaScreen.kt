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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme

/**
 * Pantalla 6: Datos de entrega (mockup "Cliente").
 * Recoge nombre, teléfono, dirección y referencia para el delivery.
 *
 * Valida que los campos obligatorios no estén vacíos.
 * Si están vacíos, activa isError = true para pintar los bordes de rojo y bloquea la navegación.
 */
@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarEntrega: (nombre: String, telefono: String, direccion: String, referencia: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    var intentoEnvio by remember { mutableStateOf(false) }

    val nombreError = intentoEnvio && nombre.isBlank()
    val telefonoError = intentoEnvio && telefono.isBlank()
    val direccionError = intentoEnvio && direccion.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

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

        CampoTexto(
            etiqueta = "Dirección de entrega",
            valor = direccion,
            onValorCambia = { direccion = it },
            placeholder = "Av. Los Olivos 123",
            isError = direccionError
        )
        if (direccionError) {
            Text(
                text = "La dirección es obligatoria",
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

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Confirmar entrega",
            onClick = {
                intentoEnvio = true
                if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()) {
                    onConfirmarEntrega(nombre, telefono, direccion, referencia)
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
        DatosEntregaScreen(onVolver = {}, onConfirmarEntrega = { _, _, _, _ -> })
    }
}
