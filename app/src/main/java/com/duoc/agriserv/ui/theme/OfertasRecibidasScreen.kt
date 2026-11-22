package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.duoc.agriserv.viewModel.OfertasRecibidasScreenViewModel

/**
 * Sección del detalle de una solicitud (vista del productor): las ofertas recibidas, de la más
 * barata a la más cara. Tocar al aplicador abre su perfil; "Aceptar oferta" pide confirmación
 * porque las demás ofertas pasan a rechazadas.
 */
@Composable
fun SeccionOfertasRecibidas(
    navController: NavController,
    idSolicitud: Int,
    viewModel: OfertasRecibidasScreenViewModel,
    sePuedeAceptar: Boolean
) {
    val estado by viewModel.estado.collectAsState()
    var ofertaPorAceptar by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(idSolicitud) {
        viewModel.cargarOfertas(idSolicitud)
    }

    ofertaPorAceptar?.let { idOferta ->
        AlertDialog(
            onDismissRequest = { ofertaPorAceptar = null },
            title = { Text("Aceptar oferta") },
            text = { Text("Al aceptar esta oferta, las demás serán rechazadas. Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        ofertaPorAceptar = null
                        viewModel.aceptarOferta(idOferta)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { ofertaPorAceptar = null }) { Text("Cancelar") }
            }
        )
    }

    Text(
        text = "Ofertas recibidas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    when {
        estado.cargando -> CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = VerdeBoton
        )

        estado.error != null -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = estado.error!!, textAlign = TextAlign.Center, color = Color.Red)
            Button(onClick = { viewModel.cargarOfertas(idSolicitud) }) { Text("Reintentar") }
        }

        estado.ofertas.isEmpty() -> Text("Aún no has recibido ofertas.")

        else -> estado.ofertas.forEach { oferta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Foto y nombre del aplicador: abren su perfil público
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(enabled = oferta.ofertanteId != null) {
                                    navController.navigate("perfilPublicoScreen/${oferta.ofertanteId}")
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black),
                                contentAlignment = Alignment.Center
                            ) {
                                if (oferta.fotoUrl != null) {
                                    AsyncImage(
                                        model = oferta.fotoUrl,
                                        contentDescription = "Foto del aplicador",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = oferta.nombre, fontWeight = FontWeight.Bold)
                                EstrellasCalificacion(oferta.calificacion, "%.1f".format(oferta.calificacion))
                            }
                        }
                        Text(
                            text = oferta.precio,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    oferta.comentario?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Estado: ${oferta.estado}", style = MaterialTheme.typography.bodySmall)

                    if (sePuedeAceptar && oferta.esPendiente) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { ofertaPorAceptar = oferta.id },
                            enabled = !estado.aceptando,
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                        ) {
                            if (estado.aceptando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Text("Aceptar oferta")
                            }
                        }
                    }
                }
            }
        }
    }

    estado.errorAceptar?.let {
        Text(text = it, color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}