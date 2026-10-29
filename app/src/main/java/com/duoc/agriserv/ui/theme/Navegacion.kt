package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.viewModel.LoginViewModel
import com.duoc.agriserv.viewModel.SignInViewModel

@Composable
fun Navegacion(loginViewModel: LoginViewModel, signInViewModel: SignInViewModel) {
    val estadoLogin by loginViewModel.estado.collectAsState()

    if (estadoLogin.cargandoSesion) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE5F0C9)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF568B3D))
        }
    } else {
        // Se calcula una sola vez: si cambiara después, el NavHost se reiniciaría
        val destinoInicial = remember {
            when (estadoLogin.sesion?.rol) {
                Rol.SOLICITANTE -> "solicitanteScreen"
                Rol.OFERTANTE -> "ofertanteScreen"
                null -> "logInScreen"
            }
        }
        val navController = rememberNavController()

        // Cierra la sesión en Room y vuelve al login borrando el historial de pantallas
        val cerrarSesion = {
            loginViewModel.cerrarSesion()
            navController.navigate("logInScreen") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }

        NavHost(navController, startDestination = destinoInicial) {
            composable("logInScreen") { LoginScreen(navController, loginViewModel) }
            composable("signInScreen") { SignInScreen(navController, signInViewModel) }
            composable("solicitanteScreen") { SolicitanteScreen(navController, cerrarSesion) }
            composable("ofertanteScreen") { OfertanteScreen(navController, cerrarSesion) }
        }
    }
}