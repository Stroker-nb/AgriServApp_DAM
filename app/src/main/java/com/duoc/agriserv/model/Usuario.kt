package com.duoc.agriserv.model

import com.google.gson.annotations.SerializedName

/**
 * Persona registrada en la app tal como la entrega la API, ya sea productor o aplicador.
 * La contraseña no está aquí: Xano la guarda cifrada y nunca la devuelve.
 */
data class Usuario(
    @SerializedName("id") val idUsuario: Int = 0,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val correo: String,
    val rol: Rol,
    val calificacion: Double = 0.0,
    val foto: Imagen? = null
)
//QUE HACE ESTA CLASE?
//Esta clase representa los datos de los usuarios en la app
//tiene 2 peculiaridades, llama a rol q es una enum class, y tiene calificacion,
// que es una variable que se utiliza solo si el usuario tiene el rol de ofertante,
//para los solicitantes se mantiene en 0.0