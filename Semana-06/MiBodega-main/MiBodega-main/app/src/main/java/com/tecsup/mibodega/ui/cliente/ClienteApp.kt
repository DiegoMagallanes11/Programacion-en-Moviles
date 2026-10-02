package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * - Maneja el tema global (modo claro / modo oscuro) recibido desde MainActivity.
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
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

/** Rutas donde se muestra la TopAppBar con el ícono del carrito */
private val rutasConTopBar = setOf(
    Rutas.INICIO,
    Rutas.DETALLE,
    Rutas.CARRITO,
    Rutas.ENTREGA,
    Rutas.PEDIDOS,
    Rutas.PERFIL
)

/** Rutas donde se muestra la BottomNavigationBar */
private val rutasConBottomBar = setOf(
    Rutas.INICIO,
    Rutas.PEDIDOS,
    Rutas.PERFIL
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteApp(
    esModoOscuro: Boolean = false,
    onModoOscuroChanged: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    
    // Historial de pedidos
    var historialPedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }

    // Cantidad total dinámica para el badge
    val cantidadTotal = carrito.sumOf { it.cantidad }

    // Ruta actual para decidir si mostrar la TopAppBar y BottomBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val mostrarTopBar = rutaActual in rutasConTopBar
    val mostrarBottomBar = rutaActual in rutasConBottomBar

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
        },
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
                    onCrearCuenta = { nombre, telefono, direccion, referencia ->
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
                                else -> null
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
                val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
                DatosEntregaScreen(
                    subtotal = subtotal,
                    onVolver = { navController.popBackStack() },
                    onConfirmarEntrega = { nombre, telefono, direccion, referencia, totalCalculado ->
                        val nuevoPedido = Pedido(
                            id = "PED-${System.currentTimeMillis().toString().takeLast(5)}",
                            fecha = "Hoy", 
                            items = carrito.toList(),
                            total = totalCalculado
                        )
                        historialPedidos = listOf(nuevoPedido) + historialPedidos
                        
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
            
            composable(Rutas.PEDIDOS) {
                PedidosScreen(historialPedidos = historialPedidos)
            }

            composable(Rutas.PERFIL) {
                PerfilScreen(
                    esModoOscuro = esModoOscuro,
                    onModoOscuroChanged = onModoOscuroChanged,
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

@Composable
private fun BarraInferior(rutaActual: String?, onNavigate: (String) -> Unit) {
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, Rutas.INICIO),
        Triple("Categorías", Icons.Default.List, "categorias_placeholder"),
        Triple("Pedidos", Icons.Default.Receipt, Rutas.PEDIDOS),
        Triple("Perfil", Icons.Default.Person, Rutas.PERFIL)
    )
    NavigationBar {
        items.forEach { (etiqueta, icono, rutaDestino) ->
            NavigationBarItem(
                selected = rutaActual == rutaDestino,
                onClick = { 
                    if (rutaDestino in setOf(Rutas.INICIO, Rutas.PEDIDOS, Rutas.PERFIL)) {
                        onNavigate(rutaDestino) 
                    }
                },
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
