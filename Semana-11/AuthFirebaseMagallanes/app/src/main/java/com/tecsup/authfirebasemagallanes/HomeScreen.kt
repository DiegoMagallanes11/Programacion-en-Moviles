package com.tecsup.authfirebasemagallanes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bienvenido/a 🎉", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Sesión iniciada como: ${auth.currentUser?.email.orEmpty()}")
            Spacer(Modifier.height(24.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { auth.signOut(); onLogout() }) {
                Text("Cerrar sesión")
            }
        }
    }
}
