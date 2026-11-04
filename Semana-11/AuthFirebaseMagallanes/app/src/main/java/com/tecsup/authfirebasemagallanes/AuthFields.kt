package com.tecsup.authfirebasemagallanes

import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

internal fun validEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

internal fun authError(error: Exception?): String = when (error) {
    is FirebaseAuthUserCollisionException -> "Este correo ya tiene una cuenta. Inicia sesión."
    is FirebaseAuthWeakPasswordException -> "La contraseña no cumple los requisitos de seguridad de Firebase."
    is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException ->
        "Revisa el correo y la contraseña."
    is FirebaseNetworkException -> "No se pudo conectar. Revisa tu conexión a Internet."
    else -> "No se pudo completar la operación. Revisa que Email/Password esté habilitado en Firebase e inténtalo de nuevo."
}

@Composable
internal fun AuthField(value: String, onChange: (String) -> Unit, label: String, password: Boolean = false, enabled: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = if (password) KeyboardType.Password else KeyboardType.Email),
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None
    )
}
