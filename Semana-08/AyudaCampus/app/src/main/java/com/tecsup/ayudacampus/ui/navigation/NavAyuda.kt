package com.tecsup.ayudacampus.ui.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tecsup.ayudacampus.AyudaApp
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.model.Rol
import com.tecsup.ayudacampus.ui.cliente.DetalleTicketScreen
import com.tecsup.ayudacampus.ui.cliente.LoginScreen
import com.tecsup.ayudacampus.ui.cliente.MisTicketsScreen
import com.tecsup.ayudacampus.ui.cliente.NuevoTicketScreen
import com.tecsup.ayudacampus.ui.cliente.PerfilScreen
import com.tecsup.ayudacampus.ui.cliente.RegistroScreen
import com.tecsup.ayudacampus.ui.cliente.SplashScreen
import com.tecsup.ayudacampus.ui.components.AppTopBar
import com.tecsup.ayudacampus.ui.soporte.AjustesScreen
import com.tecsup.ayudacampus.ui.soporte.AtenderTicketScreen
import com.tecsup.ayudacampus.ui.soporte.BandejaScreen
import com.tecsup.ayudacampus.ui.soporte.CategoriasScreen
import com.tecsup.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.tecsup.ayudacampus.ui.soporte.PanelScreen
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.AtenderViewModel
import com.tecsup.ayudacampus.viewmodel.AyudaViewModelFactory
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import com.tecsup.ayudacampus.viewmodel.CategoriasViewModel
import com.tecsup.ayudacampus.viewmodel.MisTicketsViewModel
import com.tecsup.ayudacampus.viewmodel.NuevoTicketViewModel
import com.tecsup.ayudacampus.viewmodel.PanelViewModel
import com.tecsup.ayudacampus.viewmodel.PerfilViewModel
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import com.tecsup.ayudacampus.viewmodel.TicketDetalleViewModel

// Punto de entrada de la UI: aquí se crean los ViewModels y se inyectan sus dependencias.
@Composable
fun NavAyuda(app: AyudaApp) {
    val sesion: SesionViewModel = viewModel(
        factory = AyudaViewModelFactory {
            SesionViewModel(app.repository, app.preferencias)
        }
    )
    val usuario by sesion.usuarioActual.collectAsState()
    val cargando by sesion.cargando.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val rolActivo = if (usuario?.rol == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

    AyudaTheme(rolActivo, oscuro) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                cargando -> SplashScreen()
                usuario == null -> FlujoDeAcceso(app, sesion)
                else -> {
                    val usuarioActivo = requireNotNull(usuario)
                    key(usuarioActivo.id, usuarioActivo.rol) {
                        NavegacionConSesion(app, sesion, usuarioActivo)
                    }
                }
            }
        }
    }
}

@Composable
private fun FlujoDeAcceso(
    app: AyudaApp,
    sesion: SesionViewModel
) {
    var mostrandoRegistro by rememberSaveable { mutableStateOf(false) }
    var correoRegistrado by rememberSaveable { mutableStateOf("") }

    if (mostrandoRegistro) {
        val registro: RegistroViewModel = viewModel(
            factory = AyudaViewModelFactory { RegistroViewModel(app.repository) }
        )
        RegistroScreen(
            viewModel = registro,
            onVolver = { mostrandoRegistro = false },
            onRegistrado = { correo ->
                correoRegistrado = correo
                mostrandoRegistro = false
            }
        )
    } else {
        LoginScreen(
            viewModel = sesion,
            correoInicial = correoRegistrado,
            mensajeExito = correoRegistrado.takeIf { it.isNotBlank() }?.let {
                "Cuenta creada. Ya puedes iniciar sesión."
            },
            onRegistrar = {
                correoRegistrado = ""
                mostrandoRegistro = true
            }
        )
    }
}

@Composable
private fun NavegacionConSesion(
    app: AyudaApp,
    sesion: SesionViewModel,
    usuario: Usuario
) {
    val esSoporte = usuario.rol == Rol.ADMIN.name
    val navegador = rememberNavController()
    val rutaInicio = if (esSoporte) Rutas.PANEL else Rutas.MIS_TICKETS
    val entradaActual by navegador.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val pestanaActual = when (rutaActual) {
        Rutas.DETALLE -> Rutas.MIS_TICKETS
        Rutas.ATENDER -> Rutas.BANDEJA
        Rutas.FORM_CATEGORIA -> Rutas.CATEGORIAS
        else -> rutaActual ?: rutaInicio
    }

    val destinos = if (esSoporte) {
        listOf(Rutas.PANEL, Rutas.BANDEJA, Rutas.CATEGORIAS, Rutas.AJUSTES)
    } else {
        listOf(Rutas.MIS_TICKETS, Rutas.NUEVO, Rutas.PERFIL)
    }
    val etiquetas = if (esSoporte) {
        listOf("Panel", "Tickets", "Categorías", "Ajustes")
    } else {
        listOf("Tickets", "Nuevo", "Perfil")
    }
    val iconos = if (esSoporte) {
        listOf(
            Icons.Default.Dashboard,
            Icons.Default.ConfirmationNumber,
            Icons.Default.Category,
            Icons.Default.Settings
        )
    } else {
        listOf(Icons.Default.ConfirmationNumber, Icons.Default.AddCircle, Icons.Default.Person)
    }

    val foco = LocalFocusManager.current
    val teclado = LocalSoftwareKeyboardController.current

    val navegarAPestana: (String) -> Unit = { destino ->
        teclado?.hide()
        foco.clearFocus()

        // Una pestaña siempre abre su raíz y no restaura un detalle anterior.
        if (navegador.currentDestination?.route !in destinos) {
            navegador.popBackStack(pestanaActual, false)
        }
        if (navegador.currentDestination?.route != destino) {
            if (destino == rutaInicio) {
                navegador.popBackStack(rutaInicio, false, saveState = true)
            } else {
                navegador.navigate(destino) {
                    popUpTo(navegador.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    val limpiarNavegacion: () -> Unit = {
        navegador.navigate(rutaInicio) {
            popUpTo(navegador.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    val volver = if (rutaActual !in destinos) {
        {
            teclado?.hide()
            foco.clearFocus()
            navegador.popBackStack()
            Unit
        }
    } else {
        null
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = if (esSoporte) "AyudaCampus · Soporte" else "AyudaCampus",
                volver = volver
            )
        },
        bottomBar = {
            NavigationBar(Modifier.imePadding()) {
                destinos.forEachIndexed { indice, destino ->
                    NavigationBarItem(
                        selected = pestanaActual == destino,
                        onClick = { navegarAPestana(destino) },
                        icon = { Icon(iconos[indice], contentDescription = null) },
                        label = { Text(etiquetas[indice]) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navegador,
            startDestination = rutaInicio,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            if (esSoporte) {
                grafoSoporte(
                    app = app,
                    usuario = usuario,
                    sesion = sesion,
                    navegarAPestana = navegarAPestana,
                    limpiarNavegacion = limpiarNavegacion,
                    navegar = navegador::navigate,
                    volver = {
                        navegador.popBackStack()
                        Unit
                    }
                )
            } else {
                grafoEstudiante(
                    app = app,
                    usuario = usuario,
                    sesion = sesion,
                    navegarAPestana = navegarAPestana,
                    limpiarNavegacion = limpiarNavegacion,
                    navegar = navegador::navigate
                )
            }
        }
    }
}

private fun NavGraphBuilder.grafoEstudiante(
    app: AyudaApp,
    usuario: Usuario,
    sesion: SesionViewModel,
    navegarAPestana: (String) -> Unit,
    limpiarNavegacion: () -> Unit,
    navegar: (String) -> Unit
) {
    composable(Rutas.MIS_TICKETS) {
        val viewModel: MisTicketsViewModel = viewModel(
            factory = AyudaViewModelFactory {
                MisTicketsViewModel(app.repository, usuario.id)
            }
        )
        MisTicketsScreen(
            vm = viewModel,
            nombre = usuario.nombre,
            onDetalle = { navegar("detalle/$it") },
            onNuevo = { navegarAPestana(Rutas.NUEVO) }
        )
    }

    composable(Rutas.NUEVO) {
        val viewModel: NuevoTicketViewModel = viewModel(
            factory = AyudaViewModelFactory {
                NuevoTicketViewModel(app.repository, app.preferencias, usuario.id)
            }
        )
        NuevoTicketScreen(viewModel) { navegarAPestana(Rutas.MIS_TICKETS) }
    }

    composable(Rutas.DETALLE) { entrada ->
        val ticketId = entrada.arguments?.getString("id")?.toIntOrNull() ?: 0
        val viewModel: TicketDetalleViewModel = viewModel(
            factory = AyudaViewModelFactory {
                TicketDetalleViewModel(app.repository, ticketId, usuario)
            }
        )
        DetalleTicketScreen(viewModel, usuario.id)
    }

    composable(Rutas.PERFIL) {
        val viewModel: PerfilViewModel = viewModel(
            factory = AyudaViewModelFactory {
                PerfilViewModel(app.repository, usuario.id)
            }
        )
        PerfilScreen(
            vm = viewModel,
            sesion = sesion,
            usuario = usuario,
            onCambiar = {
                limpiarNavegacion()
                sesion.cambiarPerfil()
            },
            onSalir = {
                limpiarNavegacion()
                sesion.cerrarSesion()
            }
        )
    }
}

private fun NavGraphBuilder.grafoSoporte(
    app: AyudaApp,
    usuario: Usuario,
    sesion: SesionViewModel,
    navegarAPestana: (String) -> Unit,
    limpiarNavegacion: () -> Unit,
    navegar: (String) -> Unit,
    volver: () -> Unit
) {
    composable(Rutas.PANEL) {
        val viewModel: PanelViewModel = viewModel(
            factory = AyudaViewModelFactory { PanelViewModel(app.repository) }
        )
        PanelScreen(viewModel) { navegarAPestana(Rutas.BANDEJA) }
    }

    composable(Rutas.BANDEJA) {
        val viewModel: BandejaViewModel = viewModel(
            factory = AyudaViewModelFactory { BandejaViewModel(app.repository) }
        )
        BandejaScreen(viewModel) { navegar("atender/$it") }
    }

    composable(Rutas.ATENDER) { entrada ->
        val ticketId = entrada.arguments?.getString("id")?.toIntOrNull() ?: 0
        val viewModel: AtenderViewModel = viewModel(
            factory = AyudaViewModelFactory {
                AtenderViewModel(app.repository, ticketId, usuario)
            }
        )
        AtenderTicketScreen(viewModel, usuario.id)
    }

    composable(Rutas.CATEGORIAS) {
        val viewModel: CategoriasViewModel = viewModel(
            factory = AyudaViewModelFactory { CategoriasViewModel(app.repository) }
        )
        CategoriasScreen(
            vm = viewModel,
            onEditar = { navegar("categoria/$it") },
            onNueva = { navegar("categoria/0") }
        )
    }

    composable(Rutas.FORM_CATEGORIA) { entrada ->
        val categoriaId = entrada.arguments?.getString("id")?.toIntOrNull() ?: 0
        val viewModel: CategoriasViewModel = viewModel(
            factory = AyudaViewModelFactory { CategoriasViewModel(app.repository) }
        )
        FormularioCategoriaScreen(viewModel, categoriaId) { volver() }
    }

    composable(Rutas.AJUSTES) {
        AjustesScreen(
            sesion = sesion,
            onCambiar = {
                limpiarNavegacion()
                sesion.cambiarPerfil()
            },
            onSalir = {
                limpiarNavegacion()
                sesion.cerrarSesion()
            }
        )
    }
}
