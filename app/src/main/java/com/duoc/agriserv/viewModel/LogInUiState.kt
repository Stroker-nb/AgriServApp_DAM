package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Usuario

// Estado de la pantalla de login: lo escrito, los errores por campo y la sesión
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorCredenciales: String? = null,
    // Usuario que acaba de iniciar sesión; solo sirve para mostrar el modal de bienvenida
    val usuarioLogueado: Usuario? = null,
    // Usuario con sesión guardada en Room (null si nadie ha iniciado sesión)
    val sesion: Usuario? = null,
    // Verdadero mientras Room responde si hay una sesión guardada al abrir la app
    val cargandoSesion: Boolean = true
)