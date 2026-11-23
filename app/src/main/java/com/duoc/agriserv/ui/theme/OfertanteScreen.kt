package com.duoc.agriserv.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.TipoProducto
import com.duoc.agriserv.viewModel.OfertanteScreenViewModel

/**
 * Pantalla de inicio del aplicador ("Solicitudes cercanas"): muestra las solicitudes publicadas
 * dentro del radio fijo, de la más cercana a la más lejana, y permite filtrarlas por tipo de
 * producto, momento de aplicación y superficie mínima. Desde aquí también accede a "Mis ofertas";
 * desde la barra inferior accede a su perfil o cierra la sesión.
 */
@Composable
fun OfertanteScreen(
    navController: NavController,
    viewModel: OfertanteScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val error = estado.error
    val context = LocalContext.current
    var mostrarFiltros by remember { mutableStateOf(false) }

    val avisoSinUbicacion =
        "Sin tu ubicación no se pueden ordenar por cercanía. Se muestran todas las solicitudes."

    // Le pide la ubicación aproximada al teléfono y se la entrega al ViewModel
    val buscarUbicacion = {
        pedirUbicacionActual(context) { ubicacion ->
            if (ubicacion != null) {
                viewModel.onUbicacionObtenida(ubicacion.latitude, ubicacion.longitude)
            } else {
                viewModel.onUbicacionNoDisponible(avisoSinUbicacion)
            }
        }
    }

    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) buscarUbicacion() else viewModel.onUbicacionNoDisponible(avisoSinUbicacion)
    }

    val obtenerUbicacion = {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) buscarUbicacion() else permisoLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // Cada vez que se abre la pantalla se vuelve a pedir la lista a la API y la ubicación
    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudes()
        obtenerUbicacion()
    }

    if (mostrarFiltros) {
        DialogoFiltros(
            tipoInicial = estado.filtroTipoProducto,
            aplicacionInicial = estado.filtroAplicacion,
            superficieInicial = estado.filtroSuperficieMinima,
            alAplicar = { tipo, aplicacion, superficie ->
                viewModel.aplicarFiltros(tipo, aplicacion, superficie)
                mostrarFiltros = false
            },
            alLimpiar = {
                viewModel.limpiarFiltros()
                mostrarFiltros = false
            },
            alCerrar = { mostrarFiltros = false }
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

            // Estado de la ubicación: radio activo, o aviso con opción de reintentar
            if (estado.ubicacionConocida) {
                Text(
                    text = "Mostrando solicitudes a menos de ${OfertanteScreenViewModel.RADIO_KM.toInt()} km, de la más cercana a la más lejana.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                estado.avisoUbicacion?.let { aviso ->
                    Text(text = aviso, style = MaterialTheme.typography.bodySmall, color = Color.Red)
                    TextButton(onClick = { obtenerUbicacion() }) {
                        Text("Reintentar ubicación")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("misOfertasScreen") },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Mis ofertas", fontSize = 12.sp)
                }

                Button(
                    onClick = { mostrarFiltros = true },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(if (estado.hayFiltros) "Filtros (activos)" else "Filtros", fontSize = 12.sp)
                }
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
                        text = if (estado.hayFiltros) {
                            "No hay solicitudes que cumplan con los filtros."
                        } else {
                            "Por ahora no hay solicitudes publicadas cerca de ti."
                        },
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

// Ventana con los filtros opcionales; tocar de nuevo una opción elegida la quita
@Composable
private fun DialogoFiltros(
    tipoInicial: TipoProducto?,
    aplicacionInicial: Aplicacion?,
    superficieInicial: String,
    alAplicar: (TipoProducto?, Aplicacion?, String) -> Unit,
    alLimpiar: () -> Unit,
    alCerrar: () -> Unit
) {
    var tipo by remember { mutableStateOf(tipoInicial) }
    var aplicacion by remember { mutableStateOf(aplicacionInicial) }
    var superficie by remember { mutableStateOf(superficieInicial) }

    val superficieInvalida = superficie.isNotBlank() &&
            superficie.replace(',', '.').toDoubleOrNull().let { it == null || it < 0 }

    AlertDialog(
        onDismissRequest = alCerrar,
        title = { Text("Filtros") },
        text = {
            Column {
                Text("Tipo de producto", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TipoProducto.entries.forEach { opcion ->
                        FilterChip(
                            selected = tipo == opcion,
                            onClick = { tipo = if (tipo == opcion) null else opcion },
                            label = { Text(opcion.etiqueta) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Momento de aplicación", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Aplicacion.entries.forEach { opcion ->
                        FilterChip(
                            selected = aplicacion == opcion,
                            onClick = { aplicacion = if (aplicacion == opcion) null else opcion },
                            label = { Text(opcion.etiqueta) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = superficie,
                    onValueChange = { superficie = it },
                    label = { Text("Superficie mínima (hectáreas)") },
                    singleLine = true,
                    isError = superficieInvalida,
                    supportingText = { if (superficieInvalida) Text("Ingresa un número válido") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { alAplicar(tipo, aplicacion, superficie) },
                enabled = !superficieInvalida,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = alLimpiar) { Text("Limpiar") }
        }
    )
}