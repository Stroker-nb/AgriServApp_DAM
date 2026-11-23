package com.duoc.agriserv.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Ventana a pantalla completa para marcar la ubicación del terreno: se toca el mapa o se usa
 * la ubicación aproximada del teléfono (con el permiso de ubicación). Al confirmar entrega las
 * coordenadas elegidas. Si el usuario no da el permiso, puede marcar el punto a mano en el mapa.
 */
@Composable
fun SelectorUbicacion(
    latitudInicial: Double?,
    longitudInicial: Double?,
    alConfirmar: (Double, Double) -> Unit,
    alCerrar: () -> Unit
) {
    val context = LocalContext.current
    var latitud by remember { mutableStateOf(latitudInicial) }
    var longitud by remember { mutableStateOf(longitudInicial) }
    var buscandoUbicacion by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    val buscarUbicacion = {
        buscandoUbicacion = true
        mensaje = null
        pedirUbicacionActual(context) { ubicacion ->
            buscandoUbicacion = false
            if (ubicacion != null) {
                latitud = ubicacion.latitude
                longitud = ubicacion.longitude
            } else {
                mensaje = "No se pudo obtener tu ubicación. Toca el mapa para marcarla."
            }
        }
    }

    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            buscarUbicacion()
        } else {
            mensaje = "Sin el permiso de ubicación puedes marcar el terreno tocando el mapa."
        }
    }

    val usarMiUbicacion = {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) buscarUbicacion() else permisoLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = VerdeFondo) {
            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Ubicación del terreno",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Toca el mapa para marcar el terreno, o usa tu ubicación. " +
                            "Se guarda una ubicación aproximada, no el punto exacto.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                MapaUbicacion(
                    latitud = latitud,
                    longitud = longitud,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    alElegirUbicacion = { nuevaLatitud, nuevaLongitud ->
                        latitud = nuevaLatitud
                        longitud = nuevaLongitud
                    }
                )

                mensaje?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = usarMiUbicacion,
                    enabled = !buscandoUbicacion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (buscandoUbicacion) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.MyLocation, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Usar mi ubicación")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = alCerrar) { Text("Cancelar") }
                    Button(
                        onClick = {
                            val confirmadaLatitud = latitud
                            val confirmadaLongitud = longitud
                            if (confirmadaLatitud != null && confirmadaLongitud != null) {
                                alConfirmar(confirmadaLatitud, confirmadaLongitud)
                            }
                        },
                        enabled = latitud != null && longitud != null
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}

// Le pide al teléfono su ubicación actual aproximada; entrega null si no se pudo obtener
@SuppressLint("MissingPermission")
fun pedirUbicacionActual(context: Context, alRecibir: (Location?) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context)
        .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
        .addOnSuccessListener { alRecibir(it) }
        .addOnFailureListener { alRecibir(null) }
}