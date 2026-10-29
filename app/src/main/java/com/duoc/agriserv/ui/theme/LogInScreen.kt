
package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var tipoUsuario by remember { mutableStateOf("Solicitante") }
    var modal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F0C9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "AgriServ",
            color = Color(0xFF315D29),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text("Iniciar sesión")

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Mostrar contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Tipo de usuario")

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = tipoUsuario == "Solicitante",
                onClick = { tipoUsuario = "Solicitante" }
            )
            Text("Solicitante")

            RadioButton(
                selected = tipoUsuario == "Oferente",
                onClick = { tipoUsuario = "Oferente" }
            )
            Text("Ofertante")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                if (correo.isNotBlank() &&
                    contrasena.isNotBlank()
                ) {
                    modal = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF568B3D)
            )
        ) {
            Text("Iniciar sesión")
        }

        TextButton(
            onClick = {
                navController.navigate("signInScreen")
            }
        ) {
            Text("Crear una cuenta")
        }
    }

    if (modal) {
        AlertDialog(
            onDismissRequest = { modal = false },
            title = { Text("AgriServ") },
            text = { Text("Datos ingresados correctamente") },
            confirmButton = {
                Button(onClick = { modal = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
