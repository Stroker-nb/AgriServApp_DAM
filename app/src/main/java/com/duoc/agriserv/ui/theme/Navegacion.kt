package com.duoc.agriserv.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navegacion() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "logInScreen") {
        composable("logInScreen") { LoginScreen(navController) }
        composable ("singInScreen"){ SingInScreen(navController) }
    }
}
