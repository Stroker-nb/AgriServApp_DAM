package com.duoc.agriserv

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.agriserv.ui.theme.AgriServTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgriServTheme() {
                var contraseñaVisible by remember { mutableStateOf(false) }
                var contraseña by remember {mutableStateOf("")}
                var nombre by remember { mutableStateOf("") }
                var modal by remember { mutableStateOf(false) }
                var terminos by remember { mutableStateOf(false) }
                fun abrirModal(){ modal = true }
                fun cerrarModal(){ modal = false }
                fun verificarSiNombreTieneError(): Boolean{
                    if(nombre== ""){
                        return true
                    }
                    else {
                        return false
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Correo Electronico") },
                        isError = verificarSiNombreTieneError(),
                        supportingText = {
                            if(verificarSiNombreTieneError()){
                                Text( "Debes ingresar el correo", color = Red)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))



                    OutlinedTextField(
                        value = contraseña,
                        onValueChange = { contraseña = it },
                        label = { Text("Contraseña") },
                        isError = verificarSiNombreTieneError(),
                        supportingText = {
                            if(verificarSiNombreTieneError()){
                                Text( "Debes ingresar una contraseña", color = Red)
                            }
                        },
                        // Cambia el texto entre puntos y texto plano
                        visualTransformation = if (contraseñaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        // Botón con el ojo al final del campo
                        trailingIcon = {
                            val icono = if (contraseñaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { contraseñaVisible = !contraseñaVisible }) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = if (contraseñaVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        }
                    )
                    Row (
                    ){
                        Checkbox(
                            checked = terminos,
                            onCheckedChange = { terminos = it },

                            )
                        Text ("Aceptar terminos y condiciones")}
                    Button(onClick={abrirModal()}) {
                        Text(text="Enviar")
                    }
                }
                if(modal) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = { Text("Confirmación") },
                        text = { Text("Formulario enviado correctamente") },
                        confirmButton = {
                            Button(onClick = {cerrarModal()}) { Text("OK") }
                        }
                    )
                }
            }
        }
    }
}