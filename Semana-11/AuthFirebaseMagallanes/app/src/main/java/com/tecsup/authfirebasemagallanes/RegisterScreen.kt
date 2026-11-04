package com.tecsup.authfirebasemagallanes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateBack: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    BackHandler(enabled = loading) { }
    BoxWithConstraints(Modifier.fillMaxSize().imePadding()) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).heightIn(min = maxHeight).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear cuenta ✨", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            AuthField(email, { email = it; error = null }, "Correo electrónico", enabled = !loading)
            AuthField(password, { password = it; error = null }, "Contraseña", password = true, enabled = !loading)
            AuthField(confirm, { confirm = it; error = null }, "Confirmar contraseña", password = true, enabled = !loading)
            error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
            Spacer(Modifier.height(16.dp))
            Button(
                enabled = !loading, modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val address = email.trim()
                    when {
                        address.isBlank() || password.isBlank() || confirm.isBlank() -> error = "Complete todos los campos."
                        !validEmail(address) -> error = "Ingrese un correo electrónico válido."
                        password != confirm -> error = "Las contraseñas no coinciden."
                        password.length < 6 -> error = "La contraseña debe tener al menos 6 caracteres."
                        else -> {
                            loading = true
                            auth.createUserWithEmailAndPassword(address, password).addOnCompleteListener { task ->
                                loading = false
                                if (task.isSuccessful) { password = ""; confirm = ""; onRegisterSuccess() }
                                else error = authError(task.exception)
                            }
                        }
                    }
                }
            ) { Text(if (loading) "Creando..." else "Registrarse") }
            TextButton(enabled = !loading, onClick = onNavigateBack) { Text("← Volver al login") }
        }
    }
}
