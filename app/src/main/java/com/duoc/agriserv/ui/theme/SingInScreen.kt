package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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

@Composable
fun SingInScreen() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("")}
    var rut by remember {mutableStateOf("")}
    var correoelectronico by remember {mutableStateOf("")}
    var contrasena by remember { mutableStateOf("") }


    val appellidoInvalido = apellido.isBlank()
    val nombreInvalido = nombre.isBlank()
    val rutInvalido = rut.isBlank()
    val correoInvalido = correoelectronico.isBlank()
    val contrasenaInvalida = contrasena.isBlank()

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


    }


































}