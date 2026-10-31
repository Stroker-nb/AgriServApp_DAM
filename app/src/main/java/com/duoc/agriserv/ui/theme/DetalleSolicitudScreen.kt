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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.duoc.agriserv.R
import com.duoc.agriserv.model.Rol


@Composable
fun DetalleSolicitudScreen(navController: NavController, rol: Rol?, onCerrarSesion: () -> Unit) {

    val verde = VerdeBoton
    val fondo = VerdeFondo


    var solicitudes by remember {
        mutableStateOf(
            listOf(
                "Producto: Granulado\nSuperficie: 10 hectáreas\nEstado: Pendiente"

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

            //a
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
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                text = "Detalle de la solicitud",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Image(
                                painter = painterResource(id = R.drawable.foto_cultivo),
                                contentDescription = "Foto del cultivo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = solicitud,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Información del solicitante",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Nombre: Juan Pérez")
                            Text("Teléfono: +56 9 1234 5678")
                            Text("Ubicación: Santiago, Chile")

                            Spacer(modifier = Modifier.height(15.dp))

                            Divider()

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Detalles de aplicación",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Tipo de aplicación: Terrestre")
                            Text("Disponibilidad de agua: Sí")
                            Text("Obstáculos: Árboles y cercos")
                            Text("Fecha solicitada: 30/10/2026")

                            Spacer(modifier = Modifier.height(15.dp))

                            Divider()

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Comentarios adicionales",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Se solicita realizar la aplicación " +
                                        "del producto en toda la superficie, " +
                                        "teniendo especial cuidado con los cultivos."
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = "Estado de la solicitud: Pendiente",
                                color = VerdeBoton,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            //a


        }
        BarraNavegacionAgriServ(
            onInicio = {
                // El detalle lo ven los dos roles, así que "Inicio" vuelve al home que corresponde
                val homeDelRol =
                    if (rol == Rol.OFERTANTE) "ofertanteScreen" else "solicitanteScreen"
                navController.navigate(homeDelRol) {
                    popUpTo(homeDelRol) { inclusive = true }
                }
            },
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