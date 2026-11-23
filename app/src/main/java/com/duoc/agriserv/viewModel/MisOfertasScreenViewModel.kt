package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
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
import com.duoc.agriserv.viewModel.MisOfertasScreenViewModel

/**
 * Pantalla "Mis ofertas" del aplicador: las ofertas que ha enviado, con su precio y su estado
 * (pendiente, aceptada o rechazada), y un acceso al detalle de cada solicitud.
 */
@Composable
fun MisOfertasScreen(
    navController: NavController,
    viewModel: MisOfertasScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val error = estado.error

    // Cada vez que se abre la pantalla se vuelve a pedir la lista a la API
    LaunchedEffect(Unit) {
        viewModel.cargarOfertas()
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
                text = "Mis ofertas",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

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
                        Button(onClick = { viewModel.cargarOfertas() }) {
                            Text("Reintentar")
                        }
                    }

                    estado.ofertas.isEmpty() -> Text(
                        text = "Aún no has enviado ofertas.",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )

                    else -> LazyColumn {
                        items(estado.ofertas) { oferta ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GrisImagen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (oferta.fotoUrl != null) {
                                            AsyncImage(
                                                model = oferta.fotoUrl,
                                                contentDescription = "Foto de la solicitud",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = oferta.titulo, fontWeight = FontWeight.Bold)
                                        if (oferta.detalle.isNotBlank()) Text(text = oferta.detalle)
                                        Text(text = "Tu oferta: ${oferta.precio}", fontWeight = FontWeight.Medium)
                                        Text(text = "Estado: ${oferta.estado}", style = MaterialTheme.typography.bodySmall)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = {
                                                navController.navigate("detalleSolicitudScreen/${oferta.solicitudId}")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                                        ) {
                                            Text("Ver solicitud")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        BarraNavegacionAgriServ(
            onInicio = {
                navController.navigate("ofertanteScreen") {
                    popUpTo("ofertanteScreen") { inclusive = true }
                }
            },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}