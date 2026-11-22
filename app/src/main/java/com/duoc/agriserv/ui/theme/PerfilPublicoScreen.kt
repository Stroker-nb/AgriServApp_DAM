package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.viewModel.PerfilPublicoScreenViewModel

/**
 * Perfil público de un usuario: su foto, nombre, rol y, si es aplicador, su calificación.
 * No muestra RUT ni correo.
 */
@Composable
fun PerfilPublicoScreen(
    navController: NavController,
    idUsuario: Int,
    viewModel: PerfilPublicoScreenViewModel,
    rolActual: Rol?,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val perfil = estado.perfil

    LaunchedEffect(idUsuario) { viewModel.cargarPerfil(idUsuario) }

    Column(modifier = Modifier.fillMaxSize().background(VerdeFondo)) {
        EncabezadoAgriServ()

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                estado.cargando -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = VerdeBoton
                )

                estado.error != null -> Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = estado.error!!, textAlign = TextAlign.Center, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.cargarPerfil(idUsuario) }) { Text("Reintentar") }
                }

                perfil != null -> Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        if (estado.fotoUrl != null) {
                            AsyncImage(
                                model = estado.fotoUrl,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${perfil.nombre} ${perfil.apellido}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (perfil.rol == Rol.OFERTANTE) "Aplicador" else "Productor",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // La calificación solo existe para los aplicadores
                    if (perfil.rol == Rol.OFERTANTE) {
                        Spacer(modifier = Modifier.height(12.dp))
                        EstrellasCalificacion(perfil.calificacion, "%.1f".format(perfil.calificacion))
                    }
                }
            }
        }

        BarraNavegacionAgriServ(
            onInicio = {
                val homeDelRol = if (rolActual == Rol.OFERTANTE) "ofertanteScreen" else "solicitanteScreen"
                navController.navigate(homeDelRol) { popUpTo(homeDelRol) { inclusive = true } }
            },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}