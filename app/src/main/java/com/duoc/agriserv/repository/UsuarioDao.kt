package com.duoc.agriserv.repository

import androidx.room.vo.Dao
import androidx.room.*
import com.duoc.agriserv.model.Usuario

@Dao
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