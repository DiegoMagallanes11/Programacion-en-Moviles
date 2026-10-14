package com.tecsup.ayudacampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.navigation.NavAyuda
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  val app=application as AyudaApp
  setContent {
   val listo by app.listo.collectAsState();val error by app.errorInicio.collectAsState()
   if(listo) NavAyuda(app) else AyudaTheme { SplashScreen(error) }
  }
 }
}
