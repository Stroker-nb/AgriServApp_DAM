package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SingInScreen(navController : NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correoelectronico by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("productor") }
    var contrasenaVisible by remember { mutableStateOf(false) }


    val apellidoInvalido = apellido.isBlank()
    val nombreInvalido = nombre.isBlank()
    val rutInvalido = rut.isBlank()
    val correoInvalido = correoelectronico.isBlank()
    val contrasenaInvalida = contrasena.isBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC7E29C)) //Fondo verde claro
    ) {
        // banda verde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF639A4D))
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // texto arriba en el centro
            Text(
                text = "Agri Serv",
                fontSize = 40.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 55.dp)
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // texto Abajo de agri serv
                Text(
                    text = "Crear Tu Cuenta",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 120.dp)
                )





                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Botones de selección
                    Spacer(modifier = Modifier.height(110.dp)) // Espacio

                    OutlinedButton(
                        onClick = { rolSeleccionado = "productor" },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (rolSeleccionado == "productor") Color(0xFFB5D793) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Soy un productor", color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { rolSeleccionado = "aplicador" },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (rolSeleccionado == "aplicador") Color(0xFFB5D793) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Soy un aplicador", color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Ingresa Tu nombre") },
                        isError = nombreInvalido,
                        supportingText = {
                            if (nombreInvalido) {
                                Text("Debes ingresar tu nombre", color = Color.Red)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Ingresa Tu apellido") },
                        isError = apellidoInvalido,
                        supportingText = {
                            if (apellidoInvalido) {
                                Text("Debes ingresar tu Apellido", color = Color.Red)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { rut = it },
                        label = { Text("Ingresa Tu RUT") },
                        isError = rutInvalido,
                        supportingText = {
                            if (rutInvalido) {
                                Text("Debes ingresar tu Rut", color = Color.Red)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = correoelectronico,
                        onValueChange = { correoelectronico = it },
                        label = { Text("Ingresa Tu Correo Electronico") },
                        isError = correoInvalido,
                        supportingText = {
                            if (correoInvalido) {
                                Text("Debes ingresar tu correo electronico", color = Color.Red)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text("Ingresa tu contraseña") },
                        isError = contrasenaInvalida,
                        supportingText = {
                            if (contrasenaInvalida) {
                                Text("Debes ingresar tu contraseña", color = Color.Red)
                            }
                        }
                    )

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF68875B))
                    ) {
                        Text(text = "Crear Cuenta ")

                    }
                    TextButton(
                        onClick = {
                            navController.navigate("logInScreen")
                        }
                    ) {
                        Text("Ya tengo una cuenta")
                    }


                }
            }
        }
    }
}



































