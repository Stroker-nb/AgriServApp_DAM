package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Usuario

// Estado de la pantalla de login ESTO ES LO QUE GUARDA EL LOG IN PERSISTENTEEEEEE
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorCredenciales: String? = null,
    val usuarioLogueado: Usuario? = null
)