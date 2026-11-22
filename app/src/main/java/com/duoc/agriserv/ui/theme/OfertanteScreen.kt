package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.agriserv.viewModel.OfertanteScreenViewModel

/**
 * Pantalla de inicio del aplicador ("Solicitudes cercanas"): muestra las solicitudes publicadas,
 * leídas de la API, para que pueda abrir su detalle. Desde la barra inferior accede a su perfil
 * o cierra la sesión. El botón "Filtros" todavía no tiene acción.
 */
@Composable
fun OfertanteScreen(
    navController: NavController,
    viewModel: OfertanteScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val error = estado.error

    // Cada vez que se abre la pantalla se vuelve a pedir la lista a la API
    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFondo)
    ) {

        EncabezadoAgriServ()

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {

            Text(
                text = "Solicitudes cercanas",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "En este apartado puede ver sus solicitudes cercanas a su posicion, presione Ver detalle para relizar una oferta sobre alguna de su interés. Presione filtros para ser mas especifico con su búsqueda",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Filtros", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    estado.cargando -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = VerdeBoton
                    )

                    error != null -> Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = error, textAlign = TextAlign.Center, color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.cargarSolicitudes() }) {
                            Text("Reintentar")
                        }
                    }

                    estado.solicitudes.isEmpty() -> Text(
                        text = "Por ahora no hay solicitudes publicadas.",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )

                    else -> LazyColumn {
                        items(estado.solicitudes) { solicitud ->
                            TarjetaSolicitud(
                                solicitud = solicitud,
                                textoBoton = "Ver detalle",
                                alPulsarBoton = {
                                    navController.navigate("detalleSolicitudScreen/${solicitud.id}")
                                }
                            )
                        }
                    }
                }
            }
        }

        BarraNavegacionAgriServ(
            onInicio = { },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}