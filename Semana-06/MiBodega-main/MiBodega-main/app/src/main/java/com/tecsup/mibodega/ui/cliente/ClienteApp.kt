package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                PantallaProvisional(nombre = "Bienvenida")
            }
        }
    }
}

@Composable
private fun PantallaProvisional(nombre: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ClienteAppPreview() {
    BodegaTheme {
        ClienteApp()
    }
}