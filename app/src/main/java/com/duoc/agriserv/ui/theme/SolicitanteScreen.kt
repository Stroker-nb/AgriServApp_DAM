package com.duoc.agriserv.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SolicitanteScreen(navController: NavController, onCerrarSesion: () -> Unit) {

    val verde = VerdeBoton
    val fondo = VerdeFondo


    var solicitudes by remember {
        mutableStateOf(
            listOf(
                "Producto: Granulado\nSuperficie: 10 hectáreas\nEstado: Pendiente",
                "Producto: Líquido\nSuperficie: 5 hectáreas\nEstado: Oferta recibida",
                "Producto: Granulado\nSuperficie: 14 hectáreas\nEstado: Finalizada"
            )
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
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


            Spacer(modifier = Modifier.height(15.dp))


            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(solicitudes) { solicitud ->


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Espacio para la foto de la solicitud, como en el mockup
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(solicitud)


                                Spacer(modifier = Modifier.height(8.dp))


                                Button(
                                    onClick = {
                                        navController.navigate("detalleSolicitudScreen")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = verde
                                    )
                                ) {
                                    Text("Ver detalle")
                                }
                            }
                        }
                    }
                }
            }


            Button(
                onClick = {
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
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

//QUE HACE ESTA CLASE?
//Esta clase es la pantalla/Interfaz principal de solicitante,
//Donde puede ver sus solicitudes y su estado
//De momento no tiene funcionalidad a parte de su barra de navegacion inferior,
//donde puede ir a ver su perfil
//y cerrar sesion