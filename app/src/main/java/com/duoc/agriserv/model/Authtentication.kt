package com.duoc.agriserv.model

// Datos que se envían a la API para iniciar sesión
data class Credenciales(
    val correo: String,
    val contrasena: String
)

// Respuesta de Xano al registrar o iniciar sesión: un token que identifica al usuario
data class RespuestaToken(
    val authToken: String
)