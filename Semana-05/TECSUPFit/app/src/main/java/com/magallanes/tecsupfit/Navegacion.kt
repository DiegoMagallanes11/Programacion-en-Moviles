package com.magallanes.tecsupfit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class DestinoSecundario(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : DestinoSecundario("inicio", "Clases", Icons.Default.Home)
    object Reservas : DestinoSecundario("reservas", "Mis Reservas", Icons.Default.DateRange)
    object Perfil : DestinoSecundario("perfil", "Perfil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTECSUPFit() {
    val navController = rememberNavController()
    val destinos = listOf(
        DestinoSecundario.Inicio,
        DestinoSecundario.Reservas,
        DestinoSecundario.Perfil
    )

    // Estado global de reservas usando mutableStateListOf (sin MVVM)
    val reservas = remember {
        mutableStateListOf(
            Reserva(1, "Spinning Intenso", "07:00 AM", "Carlos Ruiz", "Confirmada")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TECSUP Fit") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route

                destinos.forEach { destino ->
                    NavigationBarItem(
                        icon = { Icon(destino.icono, contentDescription = destino.titulo) },
                        label = { Text(destino.titulo) },
                        selected = rutaActual == destino.ruta,
                        onClick = {
                            if (rutaActual != destino.ruta) {
                                navController.navigate(destino.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                        inclusive = (destino.ruta == DestinoSecundario.Inicio.ruta)
                                    }
                                    launchSingleTop = true
                                    restoreState = (destino.ruta != DestinoSecundario.Inicio.ruta)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinoSecundario.Inicio.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DestinoSecundario.Inicio.ruta) {
                PantallaInicio(
                    onClaseSeleccionada = { clase ->
                        navController.navigate("detalle/${clase.id}")
                    }
                )
            }

            composable(
                route = "detalle/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId")
                val claseSeleccionada = listaClasesEjemplo.find { it.id == claseId }

                PantallaDetalle(
                    clase = claseSeleccionada,
                    onReservarClick = {
                        claseSeleccionada?.let {
                            navController.navigate("agendar/${it.id}")
                        }
                    }
                )
            }

            composable(
                route = "agendar/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId")
                val claseSeleccionada = listaClasesEjemplo.find { it.id == claseId }

                PantallaAgendar(
                    clase = claseSeleccionada,
                    onConfirmarHorario = { horario ->
                        claseSeleccionada?.let {
                            reservas.add(
                                Reserva(
                                    id = reservas.size + 1,
                                    nombreClase = it.nombre,
                                    horario = horario,
                                    entrenador = it.entrenador,
                                    estado = "Confirmada"
                                )
                            )
                        }
                        navController.navigate("confirmacion/${claseId}/${horario}")
                    }
                )
            }

            composable(
                route = "confirmacion/{claseId}/{horario}",
                arguments = listOf(
                    navArgument("claseId") { type = NavType.IntType },
                    navArgument("horario") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId")
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                val claseSeleccionada = listaClasesEjemplo.find { it.id == claseId }

                PantallaConfirmacion(
                    clase = claseSeleccionada,
                    horario = horario,
                    onIrAMisReservas = {
                        navController.navigate(DestinoSecundario.Reservas.ruta) {
                            popUpTo(DestinoSecundario.Inicio.ruta)
                        }
                    }
                )
            }

            composable(DestinoSecundario.Reservas.ruta) {
                PantallaReservas(reservas = reservas)
            }
            composable(DestinoSecundario.Perfil.ruta) {
                PantallaPerfil()
            }
        }
    }
}