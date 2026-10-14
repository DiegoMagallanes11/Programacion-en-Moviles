package com.tecsup.ayudacampus.ui.cliente
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
@Composable fun SplashScreen(error: String? = null) {
 Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) { Column(horizontalAlignment=Alignment.CenterHorizontally) {
  Text("AyudaCampus",style=MaterialTheme.typography.headlineLarge);Text("Tu campus, conectado contigo");if(error == null) CircularProgressIndicator() else Text(error)
 } }
}
