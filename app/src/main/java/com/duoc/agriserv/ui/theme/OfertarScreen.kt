package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.agriserv.R
import com.duoc.agriserv.viewModel.NuevaOfertaScreenViewModel

/**
 * Formulario del aplicador para ofertar sobre una solicitud: precio total en pesos y un
 * comentario opcional. Al enviar, un modal confirma y lleva a "Mis ofertas".
 */
@Composable
fun OfertarScreen(
    navController: NavController,
    idSolicitud: Int,
    viewModel: NuevaOfertaScreenViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val coloresCampo = coloresCampoAgriServ()

    // Cada vez que se abre el formulario empieza limpio
    LaunchedEffect(idSolicitud) {
        viewModel.reiniciar()
    }

    if (estado.ofertaCreada) {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.logo_agriserv),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Oferta enviada") },
            text = { Text("Tu oferta fue enviada al productor. Puedes revisar su estado en \"Mis ofertas\".") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate("misOfertasScreen") {
                            popUpTo("ofertanteScreen")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                ) {
                    Text("Ver mis ofertas")
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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Enviar oferta",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Indica el precio total por el servicio, en pesos chilenos. El productor comparará las ofertas y elegirá una.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::onPrecioChange,
                label = { Text("Precio total (CLP)") },
                prefix = { Text("$") },
                singleLine = true,
                isError = estado.errorPrecio != null,
                supportingText = { estado.errorPrecio?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estado.comentario,
                onValueChange = viewModel::onComentarioChange,
                label = { Text("Comentario (opcional)") },
                minLines = 3,
                isError = estado.errorComentario != null,
                supportingText = { Text(estado.errorComentario ?: "${estado.comentario.length}/300") },
                colors = coloresCampo,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.enviar(idSolicitud) },
                enabled = !estado.enviando,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (estado.enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Enviar oferta")
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