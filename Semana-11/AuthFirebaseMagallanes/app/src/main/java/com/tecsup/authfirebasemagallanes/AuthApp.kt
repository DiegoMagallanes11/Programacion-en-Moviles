package com.tecsup.authfirebasemagallanes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

private object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AuthApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val auth = remember { FirebaseAuth.getInstance() }
    val start = remember { if (auth.currentUser == null) Destinations.LOGIN else Destinations.HOME }
    val enterHome: () -> Unit = {
        navController.navigate(Destinations.HOME) {
            popUpTo(Destinations.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }
    NavHost(navController, startDestination = start, modifier = modifier) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Destinations.REGISTER) },
                onLoginSuccess = enterHome
            )
        }
        composable(Destinations.REGISTER) {
            RegisterScreen(onRegisterSuccess = enterHome, onNavigateBack = { navController.popBackStack() })
        }
        composable(Destinations.HOME) {
            HomeScreen(onLogout = {
                navController.navigate(Destinations.LOGIN) {
                    popUpTo(Destinations.HOME) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
    }
}
