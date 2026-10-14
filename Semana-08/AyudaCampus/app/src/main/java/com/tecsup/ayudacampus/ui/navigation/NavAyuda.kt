package com.tecsup.ayudacampus.ui.navigation
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.ayudacampus.AyudaApp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.viewmodel.*
import com.tecsup.ayudacampus.ui.cliente.*
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
@Composable fun NavAyuda(app: AyudaApp) {
 val sesion: SesionViewModel=viewModel(factory=AyudaViewModelFactory { SesionViewModel(app.repository,app.preferencias) })
 val usuario by sesion.usuarioActual.collectAsState();val cargando by sesion.cargando.collectAsState();val oscuro by sesion.modoOscuro.collectAsState()
 AyudaTheme(if(usuario?.rol=="ADMIN") Rol.ADMIN else Rol.CLIENTE,oscuro) { Surface {
  when { cargando -> SplashScreen();usuario==null -> LoginScreen(sesion);else -> {
   val vm: CategoriasViewModel=viewModel(factory=AyudaViewModelFactory { CategoriasViewModel(app.repository) });CategoriasScreen(vm)
  } }
 } }
}
