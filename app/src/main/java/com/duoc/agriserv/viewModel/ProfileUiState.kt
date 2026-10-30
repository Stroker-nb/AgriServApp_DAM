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
    val permisoCamaraDenegado: Boolean = false,
    val recursoDenegado: String? = null
) {
    // 0.0 significa que el ofertante todavía no tiene calificaciones
    val tieneCalificaciones: Boolean
        get() = calificacion > 0.0

    val calificacionTexto: String
        get() = "%.1f / 5".format(calificacion)
}

//QUE HACE ESTA CLASE?
//Esta clase esta hecha para que el view model de profile guarde las variables de el profile screen
//Es como si esta fuera una "Foto" que ProfileScreenView model le hace a ProfileScreen para trabajar
//Con sus datos.

// osea que en resumen, es una clase que le ayuda a nuestro view model a obtener
// datos/variables del Screen