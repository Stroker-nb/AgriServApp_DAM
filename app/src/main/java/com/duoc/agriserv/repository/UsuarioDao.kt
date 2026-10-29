package com.duoc.agriserv.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duoc.agriserv.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE sesionActiva = 1 LIMIT 1")
    suspend fun buscarUsuarioConSesion(): Usuario?

    @Query("UPDATE usuarios SET sesionActiva = 0")
    suspend fun cerrarTodasLasSesiones()

    @Query("UPDATE usuarios SET sesionActiva = 1 WHERE idUsuario = :idUsuario")
    suspend fun activarSesion(idUsuario: Int)
}