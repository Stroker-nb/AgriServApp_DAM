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
import com.duoc.agriserv.viewModel.LoginViewModel
import com.duoc.agriserv.viewModel.SignInViewModel

class MainActivity : ComponentActivity() {

    private val baseDeDatos by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "agriserv_db").build()
    }
    private val usuarioRepository by lazy { UsuarioRepository(baseDeDatos.usuarioDao()) }

    private val loginViewModel: LoginViewModel by viewModels {
        viewModelFactory { initializer { LoginViewModel(usuarioRepository) } }
    }

    private val signInViewModel: SignInViewModel by viewModels {
        viewModelFactory { initializer { SignInViewModel(usuarioRepository) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AgriServTheme {
                Navegacion(loginViewModel, signInViewModel)
            }
        }
    }
}