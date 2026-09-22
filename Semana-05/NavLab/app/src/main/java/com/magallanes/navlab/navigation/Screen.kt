package com.magallanes.navlab.navigation

sealed class Screen (val route: String){
    //Pantalla inicio
    object Home : Screen(route = "home")
    //Pantalla que muestra la lista
    object  List :Screen(route = "list")
    //Pantalla del perfil de usuario
    object Profile : Screen(route = "profile")
    object Detail : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"

    }
}