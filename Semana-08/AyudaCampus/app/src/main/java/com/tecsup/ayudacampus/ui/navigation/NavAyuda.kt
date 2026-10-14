package com.tecsup.ayudacampus.ui.navigation
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.tecsup.ayudacampus.AyudaApp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.viewmodel.*
import com.tecsup.ayudacampus.ui.cliente.*
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
@Composable fun NavAyuda(app: AyudaApp) {
 val sesion: SesionViewModel=viewModel(factory=AyudaViewModelFactory { SesionViewModel(app.repository,app.preferencias) })
 val usuario by sesion.usuarioActual.collectAsState();val cargando by sesion.cargando.collectAsState();val oscuro by sesion.modoOscuro.collectAsState()
 AyudaTheme(if(usuario?.rol=="ADMIN") Rol.ADMIN else Rol.CLIENTE,oscuro) { Surface {
  when { cargando -> SplashScreen();usuario==null -> LoginScreen(sesion);else -> key(usuario!!.id) {
   val u=usuario!!;val nav=rememberNavController()
   Scaffold(topBar={AppTopBar("AyudaCampus")}) { padding ->
    NavHost(nav,if(u.rol=="ADMIN") Rutas.CATEGORIAS else Rutas.MIS_TICKETS,Modifier.padding(padding)) {
     composable(Rutas.MIS_TICKETS) {
      val vm: MisTicketsViewModel=viewModel(factory=AyudaViewModelFactory { MisTicketsViewModel(app.repository,u.id) })
      MisTicketsScreen(vm,u.nombre,{nav.navigate("detalle/$it")},{nav.navigate(Rutas.NUEVO)})
     }
     composable(Rutas.NUEVO) {
      val vm: NuevoTicketViewModel=viewModel(factory=AyudaViewModelFactory { NuevoTicketViewModel(app.repository,app.preferencias,u.id) })
      NuevoTicketScreen(vm) { nav.popBackStack() }
     }
     composable(Rutas.DETALLE) { entry ->
      val id=entry.arguments?.getString("id")?.toIntOrNull() ?: 0
      val vm: TicketDetalleViewModel=viewModel(factory=AyudaViewModelFactory { TicketDetalleViewModel(app.repository,id,u) })
      DetalleTicketScreen(vm,u.id)
     }
     composable(Rutas.CATEGORIAS) { val vm: CategoriasViewModel=viewModel(factory=AyudaViewModelFactory { CategoriasViewModel(app.repository) });CategoriasScreen(vm) }
    }
   }
  } }
 } }
}
