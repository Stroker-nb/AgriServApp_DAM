package com.duoc.agriserv.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val calificacion: Double = 0.0
)

//QUE HACE ESTA CLASE?
//Esta clase representa los datos de los usuarios en la app
//tiene 2 peculiaridades, llama a rol q es una enum class, y tiene calificacion,
// que es una variable que se utiliza solo si el usuario tiene el rol de ofertante,
//para los solicitantes se mantiene en 0.0