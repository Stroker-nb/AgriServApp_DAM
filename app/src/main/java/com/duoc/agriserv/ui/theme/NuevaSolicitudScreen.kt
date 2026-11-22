package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.agriserv.R
import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.Obstaculo
import com.duoc.agriserv.model.TipoProducto
import com.duoc.agriserv.viewModel.NuevaSolicitudScreenViewModel
import androidx.compose.material.icons.filled.LocationOn


/**
 * Pantalla "Nueva solicitud": formulario con el que el productor publica una solicitud de
 * aplicación. Cada campo muestra su error y, al publicar con éxito, aparece un modal que
 * devuelve a la lista de solicitudes.
 */
@Composable
fun NuevaSolicitudScreen(
    navController: NavController,
    viewModel: NuevaSolicitudScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val coloresCampo = coloresCampoAgriServ()
    var mostrarSelector by remember { mutableStateOf(false) }

    if (estado.solicitudCreada) {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.logo_agriserv),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("AgriServ") },
            text = { Text("Tu solicitud se publicó correctamente") },
            confirmButton = {
                Button(onClick = {
                    viewModel.reiniciarFormulario()
                    navController.popBackStack()
                }) {
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

        if (mostrarSelector) {
            SelectorUbicacion(
                latitudInicial = estado.latitud,
                longitudInicial = estado.longitud,
                alConfirmar = { latitud, longitud ->
                    viewModel.onUbicacionElegida(latitud, longitud)
                    mostrarSelector = false
                },
                alCerrar = { mostrarSelector = false }
            )

        Column(
            modifier = Modifier
                .weight(1f)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Nueva solicitud",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            TituloCampo("Tipo de producto")
            TipoProducto.entries.forEach { producto ->
                OpcionRadio(
                    texto = producto.etiqueta,
                    seleccionada = estado.tipoProducto == producto,
                    alSeleccionar = { viewModel.onTipoProductoChange(producto) }
                )
            }
            MensajeError(estado.errorTipoProducto)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.superficie,
                onValueChange = viewModel::onSuperficieChange,
                label = { Text("Superficie (hectáreas)") },
                singleLine = true,
                isError = estado.errorSuperficie != null,
                supportingText = { MensajeError(estado.errorSuperficie) },
                trailingIcon = {
                    if (estado.errorSuperficie != null) {
                        Icon(Icons.Default.Error, contentDescription = "Campo con error")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TituloCampo("¿Tiene agua?")
            Row {
                OpcionRadio(
                    texto = "Sí",
                    seleccionada = estado.tieneAgua == true,
                    alSeleccionar = { viewModel.onTieneAguaChange(true) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                OpcionRadio(
                    texto = "No",
                    seleccionada = estado.tieneAgua == false,
                    alSeleccionar = { viewModel.onTieneAguaChange(false) }
                )
            }
            MensajeError(estado.errorTieneAgua)

            Spacer(modifier = Modifier.height(8.dp))

            TituloCampo("Aplicación")
            Row {
                Aplicacion.entries.forEach { aplicacion ->
                    OpcionRadio(
                        texto = aplicacion.etiqueta,
                        seleccionada = estado.aplicacion == aplicacion,
                        alSeleccionar = { viewModel.onAplicacionChange(aplicacion) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
            MensajeError(estado.errorAplicacion)

            Spacer(modifier = Modifier.height(8.dp))

            TituloCampo("Obstáculos (opcional)")
            Obstaculo.entries.forEach { obstaculo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = obstaculo in estado.obstaculos,
                        onCheckedChange = { viewModel.onObstaculoChange(obstaculo, it) }
                    )
                    Text(obstaculo.etiqueta)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.zona,
                onValueChange = viewModel::onZonaChange,
                label = { Text("Zona o comuna (opcional)") },
                singleLine = true,
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            TituloCampo("Ubicación del terreno")

            MapaUbicacion(
                latitud = estado.latitud,
                longitud = estado.longitud,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                alPulsar = { mostrarSelector = true }
            )

            val latitudMarcada = estado.latitud
            val longitudMarcada = estado.longitud
            Text(
                text = if (latitudMarcada != null && longitudMarcada != null) {
                    "Ubicación aproximada: ${"%.2f".format(latitudMarcada)}, ${"%.2f".format(longitudMarcada)}"
                } else {
                    "Aún no has marcado la ubicación"
                },
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedButton(onClick = { mostrarSelector = true }) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (estado.latitud == null) "Marcar en el mapa" else "Cambiar ubicación")
            }
            MensajeError(estado.errorUbicacion)

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.comentario,
                onValueChange = viewModel::onComentarioChange,
                label = { Text("Comentarios adicionales (opcional)") },
                minLines = 3,
                maxLines = 5,
                isError = estado.errorComentario != null,
                supportingText = {
                    Text(estado.errorComentario ?: "${estado.comentario.length}/300")
                },
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.publicar() },
                enabled = !estado.enviando,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (estado.enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Publicar solicitud")
                }
            }

            estado.errorServidor?.let {
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

        BarraNavegacionAgriServ(
            onInicio = { navController.popBackStack() },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}}

// Título de un grupo de opciones del formulario
@Composable
private fun TituloCampo(texto: String) {
    Text(text = texto, style = MaterialTheme.typography.labelLarge, color = VerdeOscuro)
}

// Opción de un grupo de botones de selección única
@Composable
private fun OpcionRadio(texto: String, seleccionada: Boolean, alSeleccionar: () -> Unit) {
    Row(
        modifier = Modifier.selectable(
            selected = seleccionada,
            onClick = alSeleccionar,
            role = Role.RadioButton
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = seleccionada, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto)
    }
}

// Mensaje de error de un campo; no dibuja nada si no hay error
@Composable
private fun MensajeError(mensaje: String?) {
    mensaje?.let {
        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
    }
}