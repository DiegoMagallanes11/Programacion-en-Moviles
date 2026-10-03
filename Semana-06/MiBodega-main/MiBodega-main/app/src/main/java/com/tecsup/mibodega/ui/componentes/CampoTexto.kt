package com.tecsup.mibodega.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Input con label arriba (fuera del recuadro), como en los mockups
 * de Registro y Datos de entrega. Se usa en: Registro, Datos de entrega.
 *
 * @param teclado tipo de teclado, ej. KeyboardType.Phone para el teléfono
 * @param isError si es true, pinta los bordes de rojo para indicar campo obligatorio vacío
 */
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) Color.Red else MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = teclado),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.outline,
                focusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                errorBorderColor = Color.Red
            )
        )
    }
}