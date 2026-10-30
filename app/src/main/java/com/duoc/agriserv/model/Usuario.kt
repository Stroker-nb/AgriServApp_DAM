package com.duoc.agriserv.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Una sola tabla para ambos tipos de usuario; el rol los diferencia
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val idUsuario: Int = 0,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: Rol,
    val sesionActiva: Boolean = false,
    // Solo se muestra a los ofertantes; 0.0 significa que aún no tiene calificaciones
    val calificacion: Double = 0.0
)
