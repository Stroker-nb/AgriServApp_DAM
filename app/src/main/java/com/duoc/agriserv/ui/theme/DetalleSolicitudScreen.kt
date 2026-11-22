package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
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
import com.duoc.agriserv.viewModel.DetalleSolicitudScreenViewModel

/**
 * Pantalla de detalle de una solicitud: muestra su foto, sus datos, un mapa de solo lectura con
 * su ubicación aproximada y quién la publicó. El productor puede eliminarla mientras esté
 * publicada, previa confirmación en un modal.
 */
@Composable
fun DetalleSolicitudScreen(
    navController: NavController,
    idSolicitud: Int,
    viewModel: DetalleSolicitudScreenViewModel,
    rol: Rol?,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    val solicitud = estado.solicitud
    val error = estado.error

    // Cada vez que se abre la pantalla se vuelve a pedir la solicitud a la API
    LaunchedEffect(idSolicitud) {
        viewModel.cargarSolicitud(idSolicitud)
    }

    // Cuando la solicitud se elimina, se vuelve a la lista
    LaunchedEffect(estado.solicitudEliminada) {
        if (estado.solicitudEliminada) {
            navController.popBackStack()
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Eliminar solicitud") },
            text = { Text("¿Seguro que quieres eliminar esta solicitud? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacion = false
                        viewModel.eliminarSolicitud(idSolicitud)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFondo)
    ) {

        EncabezadoAgriServ()

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
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = error, textAlign = TextAlign.Center, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.cargarSolicitud(idSolicitud) }) {
                        Text("Reintentar")
                    }
                }

                solicitud != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Detalle de solicitud",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = solicitud.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FilaDato("Producto", solicitud.producto)
                            FilaDato("Superficie", solicitud.superficie)
                            FilaDato("¿Tiene agua?", solicitud.agua)
                            FilaDato("Aplicación", solicitud.aplicacion)
                            FilaDato("Obstáculos", solicitud.obstaculos)
                            FilaDato("Zona", solicitud.zona)
                            FilaDato("Estado", solicitud.estado)
                            FilaDato("Comentarios", solicitud.comentario)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Ubicación aproximada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MapaUbicacion(
                        latitud = solicitud.latitud,
                        longitud = solicitud.longitud,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quién publicó la solicitud: mini foto de perfil y nombre
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = solicitud.solicitanteId != null) {
                                navController.navigate("perfilPublicoScreen/${solicitud.solicitanteId}")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black),
                                contentAlignment = Alignment.Center
                            ) {
                                if (solicitud.solicitanteFotoUrl != null) {
                                    AsyncImage(
                                        model = solicitud.solicitanteFotoUrl,
                                        contentDescription = "Foto del solicitante",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = solicitud.solicitanteNombre, fontWeight = FontWeight.Bold)
                                Text(text = "Productor", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    // Solo el productor puede eliminar su solicitud, y solo mientras está publicada
                    if (rol == Rol.SOLICITANTE && solicitud.sePuedeEliminar) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { mostrarConfirmacion = true },
                            enabled = !estado.eliminando,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB3261E)),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            if (estado.eliminando) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Eliminar solicitud")
                            }
                        }
                        estado.errorEliminar?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        BarraNavegacionAgriServ(
            onInicio = {
                // El detalle lo ven los dos roles, así que "Inicio" vuelve al home que corresponde
                val homeDelRol = if (rol == Rol.OFERTANTE) "ofertanteScreen" else "solicitanteScreen"
                navController.navigate(homeDelRol) {
                    popUpTo(homeDelRol) { inclusive = true }
                }
            },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}