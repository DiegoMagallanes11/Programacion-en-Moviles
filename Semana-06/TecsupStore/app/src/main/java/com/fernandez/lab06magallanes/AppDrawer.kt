package com.fernandez.lab06magallanes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawerContent(
    opcionSeleccionada: String,
    onOpcionSeleccionada: (String) -> Unit
) {
    // Hoja contenedora del menu lateral
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))

        // Opciones del menu
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = opcionSeleccionada == "Inicio",
            onClick = { onOpcionSeleccionada("Inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = opcionSeleccionada == "Mis pedidos",
            onClick = { onOpcionSeleccionada("Mis pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = opcionSeleccionada == "Favoritos",
            onClick = { onOpcionSeleccionada("Favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = opcionSeleccionada == "Perfil",
            onClick = { onOpcionSeleccionada("Perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            selected = false,
            onClick = { onOpcionSeleccionada("Cerrar sesion") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}