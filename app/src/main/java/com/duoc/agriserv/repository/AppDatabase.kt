package com.duoc.agriserv.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duoc.agriserv.model.Usuario

@Database(entities = [Usuario::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}

//QUE HACE ESTA CLASE?
//AppDataBase, como su nombre lo indica, es nuestra base de datos local que ocupa room
//Ojo por ahora, solo tiene la base de datos de usuario, ya que las bases de datos de
//Solicitudes y ofertas no fueron contempladas para esta evaluacion

