package com.tecsup.authfirebasemagallanes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    BoxWithConstraints(Modifier.fillMaxSize().imePadding()) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            Text("Programación Móvil - Tecsup", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            Text("Tecsup", color = Color(0xFF58B9DB), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Iniciar Sesión 🔐", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))
                AuthField(email, { email = it; error = null }, "Correo electrónico", enabled = !loading)
                AuthField(password, { password = it; error = null }, "Contraseña", password = true, enabled = !loading)
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
                Spacer(Modifier.height(16.dp))
                Button(
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val address = email.trim()
                        when {
                            address.isBlank() || password.isBlank() -> error = "Complete todos los campos."
                            !validEmail(address) -> error = "Ingrese un correo electrónico válido."
                            else -> {
                                loading = true
                                auth.signInWithEmailAndPassword(address, password).addOnCompleteListener { task ->
                                    loading = false
                                    if (task.isSuccessful) { password = ""; onLoginSuccess() }
                                    else error = authError(task.exception)
                                }
                            }
                        }
                    }
                ) { Text(if (loading) "Cargando..." else "Ingresar") }
                TextButton(enabled = !loading, onClick = onNavigateToRegister) { Text("¿No tienes cuenta? Regístrate aquí") }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Diego Magallanes Linares - Tecsup", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            }
        }
    }
}
