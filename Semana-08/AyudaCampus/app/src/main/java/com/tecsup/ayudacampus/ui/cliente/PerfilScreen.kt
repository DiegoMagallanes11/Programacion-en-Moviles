package com.tecsup.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.ui.components.AyudaButton
import com.tecsup.ayudacampus.ui.components.Mensaje
import com.tecsup.ayudacampus.ui.theme.Abierto
import com.tecsup.ayudacampus.ui.theme.Cerrado
import com.tecsup.ayudacampus.ui.theme.EnProceso
import com.tecsup.ayudacampus.ui.theme.Resuelto
import com.tecsup.ayudacampus.viewmodel.PerfilViewModel
import com.tecsup.ayudacampus.viewmodel.ResumenPerfil
import com.tecsup.ayudacampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    vm: PerfilViewModel,
    sesion: SesionViewModel,
    usuario: Usuario,
    onCambiar: () -> Unit,
    onSalir: () -> Unit
) {
    val resumen by vm.resumen.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val error by sesion.error.collectAsState()
    val ocupado by sesion.ocupado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)
        TarjetaUsuario(usuario)

        Text(
            "Mis tickets · ${resumen.total} en total",
            style = MaterialTheme.typography.titleMedium
        )
        TarjetaResumen(resumen)

        Text("Preferencias", style = MaterialTheme.typography.titleMedium)
        TarjetaPreferencias(
            oscuro = oscuro,
            notificaciones = notificaciones,
            cambiarOscuro = sesion::modoOscuro,
            cambiarNotificaciones = sesion::notificaciones
        )

        TarjetaCambioDePerfil(
            ocupado = ocupado,
            onCambiar = onCambiar
        )

        OutlinedButton(
            onClick = onSalir,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
        error?.let { Mensaje(it) }
    }
}

@Composable
private fun TarjetaUsuario(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        usuario.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Estudiante", style = MaterialTheme.typography.labelLarge)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Correo de acceso de la cuenta de práctica",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(usuario.correo, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun TarjetaResumen(resumen: ResumenPerfil) {
    val estados = listOf(
        EstadoResumen("Abiertos", resumen.abiertos, Abierto),
        EstadoResumen("En proceso", resumen.enProceso, EnProceso),
        EstadoResumen("Resueltos", resumen.resueltos, Resuelto),
        EstadoResumen("Cerrados", resumen.cerrados, Cerrado)
    )

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            estados.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    fila.forEach { estado ->
                        ResumenEstado(
                            estado = estado,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

private data class EstadoResumen(
    val nombre: String,
    val cantidad: Int,
    val color: Color
)

@Composable
private fun ResumenEstado(estado: EstadoResumen, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier
                .size(8.dp)
                .background(estado.color, CircleShape)
        )
        Column {
            Text(
                "${estado.cantidad}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                estado.nombre,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TarjetaPreferencias(
    oscuro: Boolean,
    notificaciones: Boolean,
    cambiarOscuro: (Boolean) -> Unit,
    cambiarNotificaciones: (Boolean) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            AjusteSwitch("Modo oscuro", oscuro, cambiarOscuro)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            AjusteSwitch(
                texto = "Notificaciones",
                valor = notificaciones,
                cambio = cambiarNotificaciones,
                descripcion = "Guarda tu preferencia. Esta versión de práctica no envía avisos del sistema."
            )
        }
    }
}

@Composable
private fun TarjetaCambioDePerfil(
    ocupado: Boolean,
    onCambiar: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Modo práctica", style = MaterialTheme.typography.titleMedium)
            Text(
                "Cambia de perfil para probar la atención de tus incidencias en este dispositivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AyudaButton("Cambiar a Soporte TI", !ocupado, onCambiar)
        }
    }
}

@Composable
fun AjusteSwitch(
    texto: String,
    valor: Boolean,
    cambio: (Boolean) -> Unit,
    descripcion: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .toggleable(
                value = valor,
                role = Role.Switch,
                onValueChange = cambio
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(texto)
            descripcion?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(checked = valor, onCheckedChange = null)
    }
}
