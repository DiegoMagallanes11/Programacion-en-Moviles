package app.src.main.java.com.example.magallanes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TemperatureDisplay() {
    // 1. Estado inicializado en 20 con remember + mutableStateOf
    var temperatura by remember { mutableStateOf(20) }

    // 2. Color condicional: rojo si > 30, azul si < 10
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
        // Muestra la temperatura
        Text(
            text = "Temperatura: $temperatura°C",
            style = MaterialTheme.typography.headlineMedium,
            color = colorTexto
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones Subir y Bajar
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { temperatura++ }) {
                Text("Subir")
            }
            Button(onClick = { temperatura-- }) {
                Text("Bajar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Resetear
        OutlinedButton(onClick = { temperatura = 20 }) {
            Text("Resetear")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTemperatureDisplay() {
    MaterialTheme {
        TemperatureDisplay()
    }

}