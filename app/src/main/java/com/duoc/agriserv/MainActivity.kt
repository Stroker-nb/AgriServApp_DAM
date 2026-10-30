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
import com.duoc.agriserv.viewModel.SignInScreenViewModelViewModel

class MainActivity : ComponentActivity() {

    private val baseDeDatos by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "agriserv_db")
            .fallbackToDestructiveMigration(dropAllTables = true) // solo en desarrollo: borra y recrea si cambia el esquema
            .build()
    }
    private val usuarioRepository by lazy { UsuarioRepository(baseDeDatos.usuarioDao()) }

    private val logInScreenViewModel: LogInScreenViewModel by viewModels {
        viewModelFactory { initializer { LogInScreenViewModel(usuarioRepository) } }
    }

    private val signInScreenViewModelViewModel: SignInScreenViewModelViewModel by viewModels {
        viewModelFactory { initializer { SignInScreenViewModelViewModel(usuarioRepository) } }
    }

    private val profileViewModel: ProfileScreenViewModel by viewModels {
        viewModelFactory { initializer { ProfileScreenViewModel(usuarioRepository) } }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AgriServTheme {
                Navegacion(logInScreenViewModel, signInScreenViewModelViewModel , profileViewModel)
            }
        }


    }
}