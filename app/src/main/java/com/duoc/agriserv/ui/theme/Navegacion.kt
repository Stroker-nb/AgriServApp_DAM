package com.duoc.agriserv.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.agriserv.viewModel.LoginViewModel
import com.duoc.agriserv.viewModel.SignInViewModel

@Composable
fun Navegacion(loginViewModel: LoginViewModel, signInViewModel: SignInViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "logInScreen") {
        composable("logInScreen") { LoginScreen(navController, loginViewModel) }
        composable("signInScreen") { SignInScreen(navController, signInViewModel) }
        composable("solicitanteScreen") { SolicitanteScreen(navController) }
        composable("ofertanteScreen") { OfertanteScreen(navController) }
    }
}