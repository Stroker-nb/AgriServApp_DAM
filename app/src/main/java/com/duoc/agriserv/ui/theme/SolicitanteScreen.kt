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
import com.duoc.agriserv.viewModel.SolicitanteScreenViewModel
import com.duoc.agriserv.viewModel.SolicitudItem

/**
 * Pantalla de inicio del productor ("Mis solicitudes"): muestra las solicitudes que ha creado,
 * leídas de la API, con su foto y su estado. Desde la barra inferior accede a su perfil o
 * cierra la sesión. El botón "Nueva solicitud" todavía no tiene acción.
 */
@Composable
fun SolicitanteScreen(
    navController: NavController,
    viewModel: SolicitanteScreenViewModel,
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
                text = "Mis solicitudes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "En este apartado puede ver sus solicitudes actuales, para crear una nueva, presione nueva solicitud." +
                        " Para ver los detalles de alguna, presione Ver detalle",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(15.dp))

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
                        text = "Aún no tienes solicitudes.",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )

                    else -> LazyColumn {
                        items(estado.solicitudes) { solicitud ->
                            TarjetaSolicitud(
                                solicitud = solicitud,
                                textoBoton = "Ver detalle",
                                alPulsarBoton = { navController.navigate("detalleSolicitudScreen/${solicitud.id}")  }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate("nuevaSolicitudScreen") },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeBoton
                )
            ) {
                Text("Nueva solicitud")
            }
        }

        BarraNavegacionAgriServ(
            onInicio = { },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}

// Tarjeta de una solicitud: foto (o un recuadro vacío), datos principales y un botón de acción
@Composable
fun TarjetaSolicitud(
    solicitud: SolicitudItem,
    textoBoton: String,
    alPulsarBoton: () -> Unit
) {
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
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (solicitud.fotoUrl != null) {
                    AsyncImage(
                        model = solicitud.fotoUrl,
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
                Text(text = solicitud.titulo, fontWeight = FontWeight.Bold)
                Text(text = solicitud.detalle)
                Text(text = solicitud.estado, style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = alPulsarBoton,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                ) {
                    Text(textoBoton)
                }
            }
        }
    }
}