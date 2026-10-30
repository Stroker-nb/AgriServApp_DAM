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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.viewModel.SignInScreenViewModelViewModel

@Composable
fun SignInScreen(navController: NavController, viewModel: SignInScreenViewModelViewModel) {

    val estado by viewModel.estado.collectAsState()
    var contrasenaVisible by remember { mutableStateOf(false) }
    var modal by remember { mutableStateOf(false) }
    val coloresCampo = coloresCampoAgriServ()

    // Cuando el ViewModel guarda la cuenta en Room, se abre el modal
    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) {
            modal = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC7E29C)) //Fondo verde claro
    ) {
        EncabezadoAgriServ()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {


            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // texto Abajo de AgriServ
                Text(
                    text = "Crear tu cuenta",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 130.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Botones de selección
                    Spacer(modifier = Modifier.height(180.dp)) // Espacio bajo los títulos

                    OutlinedButton(
                        onClick = { viewModel.onRolChange(Rol.SOLICITANTE) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (estado.rol == Rol.SOLICITANTE) Color(0xFFB5D793) else Color.Transparent
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
                        onClick = { viewModel.onRolChange(Rol.OFERTANTE) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (estado.rol == Rol.OFERTANTE) Color(0xFFB5D793) else Color.Transparent
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
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = coloresCampo,
                        value = estado.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = { Text("Ingresa tu nombre") },
                        isError = estado.errorNombre != null,
                        supportingText = {
                            estado.errorNombre?.let { Text(it, color = Color.Red) }
                        },
                        trailingIcon = {
                            if (estado.errorNombre != null) {
                                Icon(Icons.Default.Error, contentDescription = "Campo con error")
                            }
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = coloresCampo,
                        value = estado.apellido,
                        onValueChange = viewModel::onApellidoChange,
                        label = { Text("Ingresa tu apellido") },
                        isError = estado.errorApellido != null,
                        supportingText = {
                            estado.errorApellido?.let { Text(it, color = Color.Red) }
                        },
                        trailingIcon = {
                            if (estado.errorApellido != null) {
                                Icon(Icons.Default.Error, contentDescription = "Campo con error")
                            }
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = coloresCampo,
                        value = estado.rut,
                        onValueChange = viewModel::onRutChange,
                        label = { Text("Ingresa tu RUT") },
                        isError = estado.errorRut != null,
                        supportingText = {
                            estado.errorRut?.let { Text(it, color = Color.Red) }
                        },
                        trailingIcon = {
                            if (estado.errorRut != null) {
                                Icon(Icons.Default.Error, contentDescription = "Campo con error")
                            }
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = coloresCampo,
                        value = estado.correo,
                        onValueChange = viewModel::onCorreoChange,
                        label = { Text("Ingresa tu correo electrónico") },
                        isError = estado.errorCorreo != null,
                        supportingText = {
                            estado.errorCorreo?.let { Text(it, color = Color.Red) }
                        },
                        trailingIcon = {
                            if (estado.errorCorreo != null) {
                                Icon(Icons.Default.Error, contentDescription = "Campo con error")
                            }
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = coloresCampo,
                        value = estado.contrasena,
                        onValueChange = viewModel::onContrasenaChange,
                        label = { Text("Ingresa tu contraseña") },
                        isError = estado.errorContrasena != null,
                        supportingText = {
                            estado.errorContrasena?.let { Text(it, color = Color.Red) }
                        },
                        visualTransformation = if (contrasenaVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                                Icon(
                                    imageVector = if (contrasenaVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = "Mostrar contraseña"
                                )
                            }
                        }
                    )

                    Button(
                        onClick = { viewModel.registrar() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF68875B))
                    ) {
                        Text(text = "Crear cuenta")
                    }
                    TextButton(
                        onClick = {
                            navController.navigate("logInScreen")
                        }
                    ) {
                        Text("Ya tengo una cuenta")
                    }

                    if (modal) {
                        AlertDialog(
                            onDismissRequest = {
                                modal = false
                                viewModel.reiniciarFormulario()
                            },
                            title = { Text("AgriServ") },
                            text = { Text("Cuenta creada correctamente") },
                            confirmButton = {
                                Button(onClick = {
                                    modal = false
                                    viewModel.reiniciarFormulario()
                                    navController.navigate("logInScreen")
                                }) {
                                    Text("Ir a iniciar sesión")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}