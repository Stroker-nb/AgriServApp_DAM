package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.viewModel.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.duoc.agriserv.R

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {

    val estado by viewModel.estado.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var modal by remember { mutableStateOf(false) }
    val coloresCampo = coloresCampoAgriServ()

// Cuando el login es correcto, el ViewModel llena usuarioLogueado y se abre el modal
    LaunchedEffect(estado.usuarioLogueado) {
        if (estado.usuarioLogueado != null) {
            modal = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFondo),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EncabezadoAgriServ()

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Iniciar sesión")

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.85f),
            colors = coloresCampo,
            isError = estado.errorCorreo != null,
            supportingText = { Text(estado.errorCorreo ?: "") },
            trailingIcon = {
                if (estado.errorCorreo != null) {
                    Icon(Icons.Default.Error, contentDescription = "Campo con error")
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = viewModel::onContrasenaChange,
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.85f),
            colors = coloresCampo,
            isError = estado.errorContrasena != null,
            supportingText = { Text(estado.errorContrasena ?: "") },
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

        // Error general cuando el correo o la contraseña no coinciden
        estado.errorCredenciales?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { viewModel.iniciarSesion() },
            modifier = Modifier.fillMaxWidth(0.85f),
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
            onDismissRequest = {
                modal = false
                viewModel.reiniciarFormulario()
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.logo_agriserv),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("AgriServ") },
            text = { Text("Bienvenido ${estado.usuarioLogueado?.nombre ?: ""}") },
            confirmButton = {
                Button(onClick = {
                    val rol = estado.usuarioLogueado?.rol
                    modal = false
                    viewModel.reiniciarFormulario()
                    if (rol == Rol.SOLICITANTE) {
                        navController.navigate("solicitanteScreen") {
                            popUpTo("logInScreen") { inclusive = true }
                        }
                    } else if (rol == Rol.OFERTANTE) {
                        navController.navigate("ofertanteScreen") {
                            popUpTo("logInScreen") { inclusive = true }
                        }
                    }
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
    }