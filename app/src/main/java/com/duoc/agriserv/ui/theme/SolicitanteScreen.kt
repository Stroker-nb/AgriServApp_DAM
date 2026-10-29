package com.duoc.agriserv.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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


    val verde = Color(0xFF568B3D)
    val fondo = Color(0xFFE5F0C9)


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
            .padding(16.dp)
    ) {


        Text(
            text = "AgriServ",
            color = verde,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(20.dp))


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
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(solicitud)


                        Spacer(modifier = Modifier.height(8.dp))


                        Button(
                            onClick = {
                                // Aquí puedes abrir el detalle
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


        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = verde
            )
        ) {
            Text("Nueva solicitud")
        }


        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { }) {
                Text("Inicio", color = verde)
            }


            TextButton(onClick = { }) {
                Text("Información", color = verde)
            }


            TextButton(onClick = { }) {
                Text("Perfil", color = verde)
            }

            TextButton(onClick = onCerrarSesion) {
                Text("Cerrar sesión", color = verde)
            }
        }
    }
}



