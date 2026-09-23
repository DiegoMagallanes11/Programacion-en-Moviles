package com.magallanes.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Reserva(
    val id: Int,
    val nombreClase: String,
    val horario: String,
    val entrenador: String,
    val estado: String
)

@Composable
fun PantallaReservas(reservas: List<Reserva>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Reservas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (reservas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes reservas registradas aún.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(reservas) { reserva ->
                    TarjetaReserva(reserva = reserva)
                }
            }
        }
    }
}

@Composable
fun TarjetaReserva(reserva: Reserva) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = reserva.nombreClase,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Horario: ${reserva.horario}")
            Text(text = "Entrenador: ${reserva.entrenador}")
            Spacer(modifier = Modifier.height(8.dp))
            SuggestionChip(
                onClick = { },
                label = { Text(reserva.estado) }
            )
        }
    }
}