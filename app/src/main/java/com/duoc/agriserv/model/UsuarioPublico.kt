package com.duoc.agriserv.model

/**
 * Datos de un usuario que pueden ver los demás: nombre, foto y calificación.
 * No incluye RUT ni correo; la API tampoco los envía.
 */
data class UsuarioPublico(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val rol: Rol,
    val calificacion: Double = 0.0,
    val foto: Imagen? = null
)