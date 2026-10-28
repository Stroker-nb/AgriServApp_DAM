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
}