package com.magallanes.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PantallaConfirmacion(
    clase: ClaseFit?,
    horario: String,
    onIrAMisReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Éxito",
            modifier = Modifier.size(100.dp),
            tint = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Reserva confirmada!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (clase != null) {
            Text(
                text = clase.nombre,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Horario: $horario",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Entrenador: ${clase.entrenador}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onIrAMisReservas,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Ver mis reservas")
        }
    }
}