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

//QUE HACE ESTA CLASE?
//Esta clase esta hecha para que el view model de LogIn guarde las variables de el LogIn screen
//Es como si esta fuera una "Foto" que LogInScreenView model le hace a LogInScreen para trabajar
//Con sus datos.

// osea que en resumen, es una clase que le ayuda a nuestro view model a obtener
// datos/variables del Screen