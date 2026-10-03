package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme

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

/**
 * Punto de entrada de la interfaz del cliente.
 * Contiene el NavHost con el grafo de navegación entre pantallas.
 * Cada Pantalla es una función @Composable que no conoce al NavController:
 * recibe callbacks y avisa qué hacer, en vez de navegar por su cuenta.
 */
@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    Scaffold { paddingScaffold ->
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
                InicioScreen()
            }
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