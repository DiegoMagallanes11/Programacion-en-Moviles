package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.AyudaTextField
import com.tecsup.ayudacampus.ui.components.Mensaje
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    correoInicial: String = "",
    mensajeExito: String? = null,
    onRegistrar: () -> Unit = {}
) {
    var correo by rememberSaveable { mutableStateOf(correoInicial) }
    var clave by rememberSaveable { mutableStateOf("") }
    var esSoporte by rememberSaveable { mutableStateOf(false) }

    val error by viewModel.error.collectAsState()
    val ocupado by viewModel.ocupado.collectAsState()
    val oscuro by viewModel.modoOscuro.collectAsState()
    val rolSeleccionado = if (esSoporte) Rol.ADMIN else Rol.CLIENTE

    LaunchedEffect(correoInicial) {
        if (correoInicial.isNotBlank()) correo = correoInicial
    }

    AyudaTheme(rolSeleccionado, oscuro) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
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
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(56.dp)
                    )
                    Text("Bienvenido", style = MaterialTheme.typography.displaySmall)
                    Text(
                        "Ingresa para continuar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    SelectorDeRol(
                        esSoporte = esSoporte,
                        alSeleccionar = {
                            esSoporte = it
                            viewModel.limpiarError()
                        }
                    )

                    AyudaTextField(
                        valor = correo,
                        cambio = {
                            correo = it
                            viewModel.limpiarError()
                        },
                        etiqueta = "Correo"
                    )
                    AyudaTextField(
                        valor = clave,
                        cambio = {
                            clave = it
                            viewModel.limpiarError()
                        },
                        etiqueta = "Contraseña",
                        secreto = true
                    )

                    error?.let { Mensaje(it) }
                    mensajeExito?.let {
                        Text(it, color = MaterialTheme.colorScheme.primary)
                    }
                    AyudaButton(
                        texto = if (ocupado) "Ingresando…" else "Ingresar",
                        enabled = !ocupado
                    ) {
                        viewModel.login(correo, clave, rolSeleccionado)
                    }
                    if (!esSoporte) {
                        TextButton(
                            onClick = onRegistrar,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Crear una cuenta de estudiante")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorDeRol(
    esSoporte: Boolean,
    alSeleccionar: (Boolean) -> Unit
) {
    val opciones = listOf("Estudiante", "Soporte TI")

    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        opciones.forEachIndexed { indice, texto ->
            val opcionSoporte = indice == 1
            SegmentedButton(
                selected = esSoporte == opcionSoporte,
                onClick = { alSeleccionar(opcionSoporte) },
                shape = SegmentedButtonDefaults.itemShape(indice, opciones.size),
                modifier = Modifier.height(52.dp)
            ) {
                Text(texto)
            }
        }
    }
}
