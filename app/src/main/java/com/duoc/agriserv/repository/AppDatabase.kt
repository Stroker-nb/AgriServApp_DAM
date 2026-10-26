package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room3.Database

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}