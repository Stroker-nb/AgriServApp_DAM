package com.duoc.agriserv.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duoc.agriserv.model.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}