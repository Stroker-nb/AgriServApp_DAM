
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
import androidx.navigation.NavController
import com.duoc.agriserv.ui.theme.Navegacion
@Composable
fun LoginScreen(navController : NavController) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contrasenaVisible by remember { mutableStateOf(false) }
    var modal by remember { mutableStateOf(false) }
    var terminos by remember { mutableStateOf(false) }

    val correoInvalido = correo.isBlank()
    val contrasenaInvalida = contrasena.isBlank()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            isError = correoInvalido,
            supportingText = {
                if (correoInvalido) {
                    Text("Debes ingresar el correo", color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            isError = contrasenaInvalida,
            supportingText = {
                if (contrasenaInvalida) {
                    Text("Debes ingresar una contraseña", color = Color.Red)
                }
            },
            visualTransformation = if (contrasenaVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = { contrasenaVisible = !contrasenaVisible }
                ) {
                    Icon(
                        imageVector = if (contrasenaVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        contentDescription = if (contrasenaVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        }
                    )
                }
            }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = terminos,
                onCheckedChange = { terminos = it }
            )
            Text("Aceptar términos y condiciones")
        }

        Button(
            onClick = { modal = true },
            enabled = correo.isNotBlank() &&
                    contrasena.isNotBlank() &&
                    terminos
        ) {
            Text("Enviar")
        }

        Button(onClick = {navController.navigate("singInScreen")}){}
    }

    if (modal) {
        AlertDialog(
            onDismissRequest = { modal = false },
            title = { Text("Confirmación") },
            text = { Text("Formulario enviado correctamente") },
            confirmButton = {
                Button(onClick = { modal = false }) {
                    Text("OK")
                }
            }
        )
    }
}
