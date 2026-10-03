package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Objeto con las rutas de navegación de la app cliente.
 * Centralizar las rutas aquí evita repetir strings literales
 * y permite renombrar una pantalla tocando un solo lugar.
 */
object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"

    /** Construye la ruta de detalle pasando el id del producto. */
    fun detalle(productoId: Int) = "detalle/$productoId"
}

/** Rutas donde se muestra la NavigationBar inferior con los destinos principales. */
private val rutasConBottomBar = setOf(
    Rutas.INICIO,
    Rutas.CATEGORIAS,
    Rutas.PEDIDOS,
    Rutas.PERFIL
)

/**
 * Punto de entrada de la interfaz del cliente.
 * Contiene el NavHost con el grafo de navegación entre pantallas
 * y la NavigationBar inferior con los destinos principales.
 * Cada pantalla es una función @Composable que no conoce al NavController:
 * recibe callbacks y avisa qué hacer, en vez de navegar por su cuenta.
 */
@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    // Categoría seleccionada: vive arriba para que Inicio y Categorías
    // compartan el mismo filtro.
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route
    val mostrarBottomBar = rutaActual in rutasConBottomBar

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                BarraInferior(
                    rutaActual = rutaActual,
                    onNavigate = { ruta ->
                        navController.navigate(ruta) {
                            popUpTo(Rutas.INICIO)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingScaffold ->
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            modifier = Modifier.padding(paddingScaffold)
        ) {
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { usuario, contrasena ->
                        if (usuario == "admin" && contrasena == "1234") {
                            navController.navigate(Rutas.INICIO) {
                                popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                            }
                            true
                        } else {
                            false
                        }
                    },
                    onTerminos = { /* TODO: abrir términos y condiciones */ }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onVolver = { navController.popBackStack() },
                    onCrearCuenta = { _, _, _, _ ->
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.INICIO) {
                InicioScreen(
                    categoriaInicial = categoriaSeleccionada,
                    onCategoriaCambiada = { categoriaSeleccionada = it }
                )
            }

            composable(Rutas.CATEGORIAS) {
                CategoriasScreen(
                    onSeleccionarCategoria = { categoria ->
                        categoriaSeleccionada = categoria
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.PEDIDOS) {
                PedidosScreen()
            }

            composable(Rutas.PERFIL) {
                PerfilScreen(
                    onCerrarSesion = {
                        navController.navigate(Rutas.BIENVENIDA) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BarraInferior(rutaActual: String?, onNavigate: (String) -> Unit) {
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, Rutas.INICIO),
        Triple("Categorías", Icons.Default.List, Rutas.CATEGORIAS),
        Triple("Pedidos", Icons.Default.Receipt, Rutas.PEDIDOS),
        Triple("Perfil", Icons.Default.Person, Rutas.PERFIL)
    )

    NavigationBar {
        items.forEach { (etiqueta, icono, rutaDestino) ->
            NavigationBarItem(
                selected = rutaActual == rutaDestino,
                onClick = { onNavigate(rutaDestino) },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ClienteAppPreview() {
    BodegaTheme {
        ClienteApp()
    }
}