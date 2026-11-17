package com.duoc.agriserv.model

// Datos que se envían a la API al registrar un usuario; el id y la calificación los asigna Xano
data class NuevoUsuario(
    val nombre: String,
    val apellido: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: Rol
)