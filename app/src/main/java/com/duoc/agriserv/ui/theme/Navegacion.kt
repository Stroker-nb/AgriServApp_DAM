package com.duoc.agriserv.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.agriserv.R
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.viewModel.LogInScreenViewModel
import com.duoc.agriserv.viewModel.ProfileScreenViewModel
import com.duoc.agriserv.viewModel.SignInScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Navegacion(logInScreenViewModel: LogInScreenViewModel,
               signInScreenViewModel: SignInScreenViewModel,
               profileViewModel: ProfileScreenViewModel
) {
    val estadoLogin by logInScreenViewModel.estado.collectAsState()

    // La pantalla de bienvenida dura un tiempo fijo y no se repite al girar el teléfono
    var bienvenidaTerminada by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(2500)
        bienvenidaTerminada = true
    }

    if (!bienvenidaTerminada || estadoLogin.cargandoSesion) {
        PantallaBienvenida()
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
            logInScreenViewModel.cerrarSesion()
            navController.navigate("logInScreen") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }

        NavHost(navController, startDestination = destinoInicial) {
            composable("logInScreen") { LoginScreen(navController, logInScreenViewModel) }
            composable("signInScreen") { SignInScreen(navController, signInScreenViewModel) }
            composable("solicitanteScreen") { SolicitanteScreen(navController, cerrarSesion) }
            composable("ofertanteScreen") { OfertanteScreen(navController, cerrarSesion) }
            composable("profileScreen") { ProfileScreen(navController, profileViewModel, cerrarSesion) }
        }
    }
}

// Pantalla de bienvenida: el logo gira y crece, y luego aparece el nombre de la app
@Composable
private fun PantallaBienvenida() {
    val rotacion = remember { Animatable(0f) }
    val escala = remember { Animatable(0.4f) }
    val opacidadTexto = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { rotacion.animateTo(360f, tween(durationMillis = 1500, easing = FastOutSlowInEasing)) }
        launch { escala.animateTo(1f, tween(durationMillis = 1500, easing = FastOutSlowInEasing)) }
        delay(900)
        opacidadTexto.animateTo(1f, tween(durationMillis = 900))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeEncabezado),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_agriserv),
                contentDescription = "Logo de AgriServ",
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        rotationZ = rotacion.value
                        scaleX = escala.value
                        scaleY = escala.value
                    }
                    .background(Color.White, CircleShape)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "AgriServ",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.alpha(opacidadTexto.value)
            )
        }
    }
}

//QUE HACE ESTA CLASE?
//Esta clase es el enrutador de las pantallas, define como se llaman a cada una
//Dentro de ella tambien esta definida una funcion que muestra una animacion de bienvenida
//que esta configurada para aparecer cada vez que se abre la app