package com.duoc.agriserv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.duoc.agriserv.repository.AppDatabase
import com.duoc.agriserv.repository.UsuarioRepository
import com.duoc.agriserv.ui.theme.AgriServTheme
import com.duoc.agriserv.ui.theme.Navegacion
import com.duoc.agriserv.viewModel.LogInScreenViewModel
import com.duoc.agriserv.viewModel.ProfileScreenViewModel
import com.duoc.agriserv.viewModel.SignInScreenViewModel

class MainActivity : ComponentActivity() {

    // Base de datos local; "by lazy" la crea una sola vez, la primera vez que se necesita
    private val baseDeDatos by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "agriserv_db")
            .fallbackToDestructiveMigration(dropAllTables = true) // solo en desarrollo: borra y recrea si cambia el esquema
            .build()
    }

    // El repositorio recibe el DAO, así los ViewModel nunca tocan Room directamente
    private val usuarioRepository by lazy { UsuarioRepository(baseDeDatos.usuarioDao()) }

    // Cada ViewModel necesita el repositorio en su constructor, por eso se crean con una fábrica.
    // "by viewModels" los ata a la Activity: hay una sola instancia de cada uno y sobreviven
    // a girar el teléfono.
    private val logInScreenViewModel: LogInScreenViewModel by viewModels {
        viewModelFactory { initializer { LogInScreenViewModel(usuarioRepository) } }
    }

    private val signInScreenViewModel: SignInScreenViewModel by viewModels {
        viewModelFactory { initializer { SignInScreenViewModel(usuarioRepository) } }
    }

    private val profileViewModel: ProfileScreenViewModel by viewModels {
        viewModelFactory { initializer { ProfileScreenViewModel(usuarioRepository) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // La app dibuja detrás de la barra de estado y la de navegación del sistema
        enableEdgeToEdge()

        // Aquí empieza la interfaz hecha con Compose
        setContent {
            AgriServTheme {
                Navegacion(logInScreenViewModel, signInScreenViewModel, profileViewModel)
            }
        }
    }
}

//QUE HACE ESTA CLASE?
//Bueno este es el archivo main, el que hace correr la app, en el se define una funcion para que se
//genere la base de datos (ojo una sola vez por si no esta creada)
//tambien se crean los objetos viewModel, asi se crean apenas se corre la app y gracias a que
//Navegacion las definimos como parametros se les pasan a las pantallas por ahi
