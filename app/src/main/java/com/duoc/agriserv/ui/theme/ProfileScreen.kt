package com.duoc.agriserv.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.duoc.agriserv.R
import com.duoc.agriserv.viewModel.ProfileScreenViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    // Cada vez que se abre la pantalla se vuelven a leer los datos desde Room
    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    // De momento solo se abre la cámara: la foto tomada todavía no se guarda ni se muestra
    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { }

    // Si el usuario concede el permiso se abre la cámara; si no, el ViewModel muestra el modal
    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            camaraLauncher.launch(null)
        } else {
            viewModel.onPermisoCamaraDenegado()
        }
    }

    if (estado.permisoCamaraDenegado) {
        AlertDialog(
            onDismissRequest = { viewModel.cerrarMensajePermiso() },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.logo_agriserv),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("AgriServ") },
            text = {
                Text("Para tomar tu foto de perfil necesitamos el permiso de la cámara. Puedes activarlo desde los ajustes del teléfono.")
            },
            confirmButton = {
                Button(onClick = { viewModel.cerrarMensajePermiso() }) {
                    Text("Aceptar")
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Mi cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Foto de perfil con el botón de la cámara
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }

                FilledIconButton(
                    onClick = {
                        val tienePermiso = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (tienePermiso) {
                            camaraLauncher.launch(null)
                        } else {
                            permisoLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Tomar foto de perfil")
                }
            }

            if (estado.cargado) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = estado.nombreCompleto,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = estado.tipoCuenta)

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Datos personales",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FilaDato("Nombre", estado.nombre)
                        FilaDato("Apellido", estado.apellido)
                        FilaDato("Correo electrónico", estado.correo)
                        FilaDato("RUT", estado.rut)
                    }
                }

                // La calificación solo existe para los ofertantes
                if (estado.esOfertante) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Calificación",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (estado.tieneCalificaciones) {
                                EstrellasCalificacion(estado.calificacion, estado.calificacionTexto)
                            } else {
                                Text("Sin calificaciones aún")
                            }
                        }
                    }
                }
            }
        }

        BarraNavegacionAgriServ(
            onInicio = { navController.popBackStack() },
            onPerfil = { },
            onCerrarSesion = onCerrarSesion
        )
    }
}

// Una fila de la tarjeta de datos personales: etiqueta pequeña y valor debajo
@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = VerdeOscuro
        )
        Text(text = valor, style = MaterialTheme.typography.bodyLarge)
    }
}

// Cinco estrellas (llenas, medias o vacías) y la nota ya formateada
@Composable
private fun EstrellasCalificacion(calificacion: Double, textoNota: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (posicion in 1..5) {
            val icono = when {
                calificacion >= posicion -> Icons.Default.Star
                calificacion >= posicion - 0.5 -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color(0xFFFFC107)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(textoNota)
    }
}

