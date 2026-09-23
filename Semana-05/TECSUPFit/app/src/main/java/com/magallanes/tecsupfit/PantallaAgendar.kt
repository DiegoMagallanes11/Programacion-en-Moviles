package com.magallanes.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendar(
    clase: ClaseFit?,
    onConfirmarHorario: (String) -> Unit
) {
    if (clase == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Clase no encontrada.")
        }
        return
    }

    // Estado para guardar la opción única seleccionada
    var horarioSeleccionado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Seleccionar Horario",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Clase: ${clase.nombre}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Elige un horario disponible:",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selección de opción única
            clase.horariosDisponibles.forEach { horario ->
                FilterChip(
                    selected = horarioSeleccionado == horario,
                    onClick = { horarioSeleccionado = horario },
                    label = { Text(horario) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        Button(
            onClick = {
                horarioSeleccionado?.let { horario ->
                    onConfirmarHorario(horario)
                }
            },
            enabled = horarioSeleccionado != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Continuar a Confirmación")
        }
    }
}