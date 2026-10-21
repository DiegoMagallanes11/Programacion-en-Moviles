package com.tecsup.ayudacampus.ui.navigation
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.tecsup.ayudacampus.AyudaApp
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.viewmodel.*
import com.tecsup.ayudacampus.ui.cliente.*
import com.tecsup.ayudacampus.ui.soporte.*
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.theme.AyudaTheme

// Composition root: inject dependencies here; screen Composables receive only ViewModels.
@Composable fun NavAyuda(app: AyudaApp) {
 val sesion: SesionViewModel=viewModel(factory=AyudaViewModelFactory { SesionViewModel(app.repository,app.preferencias) })
 val usuario by sesion.usuarioActual.collectAsState();val cargando by sesion.cargando.collectAsState();val oscuro by sesion.modoOscuro.collectAsState()
 AyudaTheme(if(usuario?.rol=="ADMIN") Rol.ADMIN else Rol.CLIENTE,oscuro) {
  Surface(Modifier.fillMaxSize(),color=MaterialTheme.colorScheme.background) {
   when {
    cargando -> SplashScreen()
    usuario==null -> LoginScreen(sesion)
    else -> key(usuario!!.id,usuario!!.rol) {
     val u=usuario!!;val admin=u.rol=="ADMIN";val nav=rememberNavController()
     val home=if(admin) Rutas.PANEL else Rutas.MIS_TICKETS
     val entry by nav.currentBackStackEntryAsState();val ruta=entry?.destination?.route
     val destinos=if(admin) listOf(Rutas.PANEL,Rutas.BANDEJA,Rutas.CATEGORIAS,Rutas.AJUSTES) else listOf(Rutas.MIS_TICKETS,Rutas.NUEVO,Rutas.PERFIL)
     val etiquetas=if(admin) listOf("Panel","Tickets","Categorías","Ajustes") else listOf("Tickets","Nuevo","Perfil")
     val iconos=if(admin) listOf(Icons.Default.Dashboard,Icons.Default.ConfirmationNumber,Icons.Default.Category,Icons.Default.Settings) else listOf(Icons.Default.ConfirmationNumber,Icons.Default.AddCircle,Icons.Default.Person)
     val foco=LocalFocusManager.current
     val teclado=LocalSoftwareKeyboardController.current
     val ir: (String) -> Unit = { destino ->
      teclado?.hide()
      foco.clearFocus()
      if(nav.currentDestination?.route != destino) {
       nav.navigate(destino) {
        popUpTo(nav.graph.findStartDestination().id) { saveState=true }
        launchSingleTop=true
        restoreState=true
       }
      }
     }
     val limpiar: () -> Unit = { nav.navigate(home) { popUpTo(nav.graph.id) { inclusive=true };launchSingleTop=true } }
     Scaffold(
      containerColor=MaterialTheme.colorScheme.background,
      topBar={AppTopBar(if(admin) "AyudaCampus · Soporte" else "AyudaCampus",if(ruta !in destinos) ({teclado?.hide();foco.clearFocus();nav.popBackStack();Unit}) else null)},
      bottomBar={NavigationBar(Modifier.imePadding()) {
       destinos.forEachIndexed { i,destino -> NavigationBarItem(selected=ruta==destino,onClick={ir(destino)},icon={Icon(iconos[i],null)},label={Text(etiquetas[i])}) }
      }}
     ) { padding ->
      NavHost(nav,home,Modifier.padding(padding).consumeWindowInsets(padding)) {
       if(!admin) {
        composable(Rutas.MIS_TICKETS) {
         val vm: MisTicketsViewModel=viewModel(factory=AyudaViewModelFactory { MisTicketsViewModel(app.repository,u.id) })
         MisTicketsScreen(vm,u.nombre,{nav.navigate("detalle/$it")},{ir(Rutas.NUEVO)})
        }
        composable(Rutas.NUEVO) {
         val vm: NuevoTicketViewModel=viewModel(factory=AyudaViewModelFactory { NuevoTicketViewModel(app.repository,app.preferencias,u.id) })
         NuevoTicketScreen(vm) { ir(Rutas.MIS_TICKETS) }
        }
        composable(Rutas.DETALLE) { e ->
         val id=e.arguments?.getString("id")?.toIntOrNull() ?: 0
         val vm: TicketDetalleViewModel=viewModel(factory=AyudaViewModelFactory { TicketDetalleViewModel(app.repository,id,u) });DetalleTicketScreen(vm,u.id)
        }
        composable(Rutas.PERFIL) {
         val vm: PerfilViewModel=viewModel(factory=AyudaViewModelFactory { PerfilViewModel(app.repository,u.id) })
         PerfilScreen(vm,sesion,u,{limpiar();sesion.cambiarPerfil()},{limpiar();sesion.cerrarSesion()})
        }
       } else {
        composable(Rutas.PANEL) { val vm: PanelViewModel=viewModel(factory=AyudaViewModelFactory { PanelViewModel(app.repository) });PanelScreen(vm) { ir(Rutas.BANDEJA) } }
        composable(Rutas.BANDEJA) { val vm: BandejaViewModel=viewModel(factory=AyudaViewModelFactory { BandejaViewModel(app.repository) });BandejaScreen(vm) { nav.navigate("atender/$it") } }
        composable(Rutas.ATENDER) { e ->
         val id=e.arguments?.getString("id")?.toIntOrNull() ?: 0
         val vm: AtenderViewModel=viewModel(factory=AyudaViewModelFactory { AtenderViewModel(app.repository,id,u) });AtenderTicketScreen(vm,u.id)
        }
        composable(Rutas.CATEGORIAS) {
         val vm: CategoriasViewModel=viewModel(factory=AyudaViewModelFactory { CategoriasViewModel(app.repository) });CategoriasScreen(vm,{nav.navigate("categoria/$it")},{nav.navigate("categoria/0")})
        }
        composable(Rutas.FORM_CATEGORIA) { e ->
         val id=e.arguments?.getString("id")?.toIntOrNull() ?: 0
         val vm: CategoriasViewModel=viewModel(factory=AyudaViewModelFactory { CategoriasViewModel(app.repository) });FormularioCategoriaScreen(vm,id) { nav.popBackStack() }
        }
        composable(Rutas.AJUSTES) { AjustesScreen(sesion,{limpiar();sesion.cambiarPerfil()},{limpiar();sesion.cerrarSesion()}) }
       }
      }
     }
    }
   }
  }
 }
}
