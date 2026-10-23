package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SingInScreen() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correoelectronico by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("productor") }
    var contrasenaVisible by remember { mutableStateOf(false) }


    val appellidoInvalido = apellido.isBlank()
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
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Ingresa Tu nombre") },
                        isError = nombreInvalido,
                        supportingText = {
                            if (correoInvalido) {
                                Text("Debes ingresar tu nombre", color = Color.Red)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Ingresa Tu apellido") },
                        isError = appellidoInvalido,
                        supportingText = {
                            if (appellidoInvalido) {
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
                            if (contrasenaInvalida) {
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


                }
            }
        }
    }
}


































