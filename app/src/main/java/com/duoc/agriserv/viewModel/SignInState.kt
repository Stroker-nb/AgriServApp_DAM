package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Rol

// Estado de la pantalla de registro: lo que escribió el usuario y los errores por campo
data class SignInUiState(
    val nombre: String = "",
    val apellido: String = "",
    val rut: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val rol: Rol = Rol.SOLICITANTE,
    val errorNombre: String? = null,
    val errorApellido: String? = null,
    val errorRut: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val registroExitoso: Boolean = false
) {
    val hayErrores: Boolean
        get() = listOf(errorNombre, errorApellido, errorRut, errorCorreo, errorContrasena)
            .any { it != null }
}


//QUE HACE ESTA CLASE?
//Esta clase esta hecha para que el view model de SignIn guarde las variables de el SignIn screen
//Es como si esta fuera una "Foto" que SignInScreenView model le hace a SignInScreen para trabajar
//Con sus datos.

// osea que en resumen, es una clase que le ayuda a nuestro view model a obtener
// datos/variables del Screen