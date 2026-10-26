package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario
import androidx.room.*
import com.duoc.agriserv.repository.UsuarioDao

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}