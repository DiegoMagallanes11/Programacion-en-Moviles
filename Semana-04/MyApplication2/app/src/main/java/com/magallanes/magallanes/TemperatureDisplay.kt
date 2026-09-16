package com.magallanes.magallanes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TemperatureDisplay() {
    var temperatura by remember { mutableStateOf(20) }

    val colorTexto = when {
        temperatura > 30 -> Color.Red
        temperatura < 10 -> Color.Blue
        else -> MaterialTheme.colorScheme.onBackground
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Temperatura: $temperatura °C",
            style = MaterialTheme.typography.headlineMedium,
            color = colorTexto
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { temperatura++ }) {
                Text("Subir")
            }
            Button(onClick = { temperatura-- }) {
                Text("Bajar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { temperatura = 20 }) {
            Text("Resetear")
        }
    }
}