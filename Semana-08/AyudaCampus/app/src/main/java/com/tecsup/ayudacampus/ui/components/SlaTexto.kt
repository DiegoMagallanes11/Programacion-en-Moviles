package com.tecsup.ayudacampus.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tecsup.ayudacampus.model.*

@Composable fun SlaTexto(sla: Sla) {
 Text(sla.texto, style=MaterialTheme.typography.bodySmall,
  color=when(sla.situacion) {
   SituacionSla.VENCIDO -> MaterialTheme.colorScheme.error
   SituacionSla.ACTIVO -> MaterialTheme.colorScheme.primary
   SituacionSla.FINALIZADO -> MaterialTheme.colorScheme.onSurfaceVariant
  })
}
