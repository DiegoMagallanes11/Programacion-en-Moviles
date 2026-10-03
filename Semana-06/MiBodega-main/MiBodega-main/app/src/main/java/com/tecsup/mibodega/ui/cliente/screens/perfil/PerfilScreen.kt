package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla de Perfil (mockup "Cliente").
 * Muestra los datos del usuario y permite cerrar sesión.
 */
@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit
) {
    Scaffold { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Foto de perfil",
                    tint = VerdeBodega,
                    modifier = Modifier
                        .size(96.dp)
                        .background(GrisClaro, CircleShape)
                        .padding(4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Juan Pérez",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Cliente registrado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrisClaro, RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DatoPerfil(
                    icono = Icons.Default.Phone,
                    etiqueta = "Teléfono",
                    valor = "987 654 321"
                )

                HorizontalDivider()

                DatoPerfil(
                    icono = Icons.Default.LocationOn,
                    etiqueta = "Dirección de entrega",
                    valor = "Av. Los Olivos 123"
                )
            }

            Spacer(Modifier.height(28.dp))

            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DatoPerfil(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.size(12.dp))

        Column {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(onCerrarSesion = {})
    }
}