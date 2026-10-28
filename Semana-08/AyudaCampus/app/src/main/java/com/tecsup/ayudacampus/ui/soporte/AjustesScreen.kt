package com.tecsup.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.ui.cliente.AjusteSwitch
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.Mensaje
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    sesion: SesionViewModel,
    onCambiar: () -> Unit,
    onSalir: () -> Unit
) {
    val oscuro by sesion.modoOscuro.collectAsState()
    val recibirTickets by sesion.recibirTickets.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()
    val error by sesion.error.collectAsState()
    val ocupado by sesion.ocupado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineMedium)
        Text(
            usuario?.nombre ?: "Soporte TI",
            style = MaterialTheme.typography.titleLarge
        )
        Text(usuario?.correo.orEmpty())

        AjusteSwitch("Modo oscuro", oscuro, sesion::modoOscuro)
        AjusteSwitch(
            texto = "Notificaciones",
            valor = notificaciones,
            cambio = sesion::notificaciones,
            descripcion = "Guarda tu preferencia. Esta versión de práctica no envía avisos del sistema."
        )
        AjusteSwitch(
            texto = "Recibir tickets",
            valor = recibirTickets,
            cambio = sesion::recibirTickets,
            descripcion = "Permite que los estudiantes creen nuevas solicitudes."
        )

        if (!recibirTickets) {
            Text("Mesa de ayuda cerrada: los estudiantes no pueden crear tickets.")
        }

        HorizontalDivider()
        Text("Modo práctica", style = MaterialTheme.typography.titleMedium)
        AyudaButton("Cambiar a Estudiante", !ocupado, onCambiar)
        OutlinedButton(
            onClick = onSalir,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
        error?.let { Mensaje(it) }
    }
}
