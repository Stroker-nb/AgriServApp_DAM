package com.duoc.agriserv.repository

import androidx.room3.*
import com.duoc.agriserv.model.Usuario

@androidx.room3.Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios LIMIT 3")
    suspend fun getAll(): List<Usuario>

    @Insert
    suspend fun insert(usuario: Usuario)

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}