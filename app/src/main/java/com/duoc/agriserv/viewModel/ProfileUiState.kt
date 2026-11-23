package com.duoc.agriserv.viewModel

// Estado de la pantalla de perfil: datos ya listos para mostrar
data class ProfileUiState(
    val cargado: Boolean = false,
    val nombreCompleto: String = "",
    val tipoCuenta: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rut: String = "",
    val esOfertante: Boolean = false,
    val calificacion: Double = 0.0,
    val recursoDenegado: String? = null,
    val fotoUrl: String? = null,
    val subiendoFoto: Boolean = false,
    val errorFoto: String? = null
) {
    // 0.0 significa que el ofertante todavía no tiene calificaciones
    val tieneCalificaciones: Boolean
        get() = calificacion > 0.0

    val calificacionTexto: String
        get() = "%.1f / 5".format(calificacion)
}

//QUE HACE ESTA CLASE?
//Esta clase está hecha para que el ViewModel de Profile guarde las variables de la ProfileScreen.
//Es como una "foto" que ProfileScreenViewModel le entrega a ProfileScreen para trabajar
//con sus datos.
//En resumen, es una clase que le ayuda a nuestro ViewModel a manejar los
//datos/variables del Screen.