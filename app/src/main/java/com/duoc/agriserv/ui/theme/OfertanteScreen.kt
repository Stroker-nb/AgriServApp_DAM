package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OfertanteScreen(navController: NavController, onCerrarSesion: () -> Unit) {

    val verde = VerdeBoton
    val fondo = VerdeFondo

    var solicitudes by remember {
        mutableStateOf(
            listOf(
                "Producto: Granulado\nSuperficie: 10h\nDiurna",
                "Producto: Líquido\nSuperficie: 5h\nDiurna",
                "Producto: Granulado\nSuperficie: 14h\nNocturna",
                "Producto: Granulado\nSuperficie: 14h\nNocturna"
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
                text = "Solicitudes cercanas",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Aquí puedes abrir los filtros
                },
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Filtros", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(solicitudes) { solicitud ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
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

                            Text(
                                text = solicitud,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp
                            )

                            Button(
                                onClick = {
                                    // Aquí puedes abrir la oferta
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = verde
                                )
                            ) {
                                Text("Ofertar", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        //NO CAMBIAR ESTA BARRA
        BarraNavegacionAgriServ(
            onInicio = { },
            onPerfil = { navController.navigate("profileScreen") },
            onCerrarSesion = onCerrarSesion
        )
    }
}

//QUE HACE ESTA CLASE?
//Esta clase es la pantalla/Interfaz principal de ofertante,
//donde le aparecen las solicitudes cercanas a las que puede ofertar
//De momento no tiene funcionalidad a parte de su barra de navegacion inferior,
//donde puede ir a ver su perfil
//y cerrar sesion

