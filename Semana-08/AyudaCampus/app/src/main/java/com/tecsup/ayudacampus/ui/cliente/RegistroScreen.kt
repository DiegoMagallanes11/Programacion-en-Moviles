package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.Mensaje
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onVolver: () -> Unit,
    onRegistrado: (String) -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.correoCreado) {
        estado.correoCreado?.let { correo ->
            viewModel.confirmarNavegacion()
            onRegistrado(correo)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.displaySmall)
            Text(
                "Registra una cuenta de estudiante",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AyudaTextField(
                valor = estado.nombre,
                cambio = viewModel::cambiarNombre,
                etiqueta = "Nombre completo",
                error = estado.errores["nombre"]
            )
            AyudaTextField(
                valor = estado.correo,
                cambio = viewModel::cambiarCorreo,
                etiqueta = "Correo",
                error = estado.errores["correo"]
            )
            AyudaTextField(
                valor = estado.clave,
                cambio = viewModel::cambiarClave,
                etiqueta = "Contraseña",
                error = estado.errores["clave"],
                secreto = true
            )
            AyudaTextField(
                valor = estado.confirmacion,
                cambio = viewModel::cambiarConfirmacion,
                etiqueta = "Confirmar contraseña",
                error = estado.errores["confirmacion"],
                secreto = true
            )

            estado.errores["general"]?.let { Mensaje(it) }
            AyudaButton(
                texto = if (estado.registrando) "Creando cuenta…" else "Crear cuenta",
                enabled = !estado.registrando,
                onClick = viewModel::registrar
            )
            OutlinedButton(
                onClick = onVolver,
                enabled = !estado.registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio de sesión")
            }
        }
    }
}
