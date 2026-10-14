package com.tecsup.ayudacampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.viewmodel.*
class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  val app = application as AyudaApp
  setContent {
   val listo by app.listo.collectAsState(); val error by app.errorInicio.collectAsState()
   AyudaTheme { Surface { if(!listo) SplashScreen(error) else {
    val vm: CategoriasViewModel = viewModel(factory=AyudaViewModelFactory { CategoriasViewModel(app.repository) })
    CategoriasScreen(vm)
   } } }
  }
 }
}
