package com.duoc.agriserv.model

data class Usuario(
    val idUsuario: Int = 0,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: Rol,
    val calificacion: Double = 0.0
)

//QUE HACE ESTA CLASE?
//Esta clase representa los datos de los usuarios en la app
//tiene 2 peculiaridades, llama a rol q es una enum class, y tiene calificacion,
// que es una variable que se utiliza solo si el usuario tiene el rol de ofertante,
//para los solicitantes se mantiene en 0.0