package com.tecsup.mibodega.ui.cliente

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * - Tiene un Scaffold con TopAppBar que muestra un BadgedBox con la
 *   cantidad total de productos en el carrito de forma dinámica.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 *
 * Credenciales estáticas de login: usuario "admin", contraseña "1234".
 */
private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

/** Rutas donde se muestra la TopAppBar con el ícono del carrito */
private val rutasConTopBar = setOf(
    Rutas.INICIO,
    Rutas.DETALLE,
    Rutas.CARRITO,
    Rutas.ENTREGA
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    // Cantidad total dinámica para el badge
    val cantidadTotal = carrito.sumOf { it.cantidad }

    // Ruta actual para decidir si mostrar la TopAppBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val mostrarTopBar = rutaActual in rutasConTopBar

    Scaffold(
        topBar = {
            if (mostrarTopBar) {
                TopAppBar(
                    title = { Text("Mi Bodega", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = {
                            if (rutaActual != Rutas.CARRITO) {
                                navController.navigate(Rutas.CARRITO)
                            }
                        }) {
                            BadgedBox(
                                badge = {
                                    if (cantidadTotal > 0) {
                                        Badge { Text("$cantidadTotal") }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito"
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { paddingScaffold ->
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            modifier = androidx.compose.ui.Modifier.padding(paddingScaffold)
        ) {
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { usuario, contrasena ->
                        // Validación de credenciales estáticas
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
                    onCrearCuenta = { nombre, telefono, direccion, referencia ->
                        // TODO: guardar estos datos cuando exista el registro real
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.INICIO) {
                InicioScreen(
                    cantidadCarrito = cantidadTotal,
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onProductoClick = { producto ->
                        navController.navigate(Rutas.detalle(producto.id))
                    },
                    onAgregarProducto = { producto ->
                        carrito = agregarOSumarProducto(carrito, producto, 1)
                    }
                )
            }

            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                val producto = listaProductosFake.first { it.id == productoId }

                DetalleProductoScreen(
                    producto = producto,
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                        carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                        navController.popBackStack()
                    }
                )
            }

            composable(Rutas.CARRITO) {
                CarritoScreen(
                    carrito = carrito,
                    onVolver = { navController.popBackStack() },
                    onIncrementar = { producto ->
                        carrito = carrito.map {
                            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                        }
                    },
                    onDecrementar = { producto ->
                        carrito = carrito.mapNotNull {
                            when {
                                it.producto.id != producto.id -> it
                                it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                                else -> null // si llega a 0, se elimina de la lista
                            }
                        }
                    },
                    onEliminar = { producto ->
                        carrito = carrito.filterNot { it.producto.id == producto.id }
                    },
                    onContinuarPedido = { navController.navigate(Rutas.ENTREGA) }
                )
            }

            composable(Rutas.ENTREGA) {
                DatosEntregaScreen(
                    onVolver = { navController.popBackStack() },
                    onConfirmarEntrega = { nombre, telefono, direccion, referencia ->
                        // TODO: guardar datos de entrega cuando exista el backend real
                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO)
                        }
                    }
                )
            }

            composable(Rutas.CONFIRMACION) {
                ConfirmacionScreen(
                    onVolverAlInicio = {
                        carrito = emptyList()
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Si el producto ya está en el carrito, le suma la cantidad;
 * si no, lo agrega como un ItemCarrito nuevo.
 */
private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}