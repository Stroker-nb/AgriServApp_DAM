package com.duoc.agriserv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.duoc.agriserv.repository.UsuarioRepository
import com.duoc.agriserv.ui.theme.AgriServTheme
import com.duoc.agriserv.ui.theme.Navegacion
import com.duoc.agriserv.viewModel.LogInScreenViewModel
import com.duoc.agriserv.viewModel.ProfileScreenViewModel
import com.duoc.agriserv.viewModel.SignInScreenViewModel
import com.duoc.agriserv.remote.RetrofitInstance
import com.duoc.agriserv.repository.SesionPreferences
class MainActivity : ComponentActivity() {
    private val sesionPreferences by lazy {
        SesionPreferences(getSharedPreferences("agriserv_sesion", MODE_PRIVATE))
    }

    private val usuarioRepository by lazy { UsuarioRepository(sesionPreferences) }

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
        // El cliente de la API lee el token guardado para enviarlo en cada petición
        RetrofitInstance.obtenerToken = { sesionPreferences.obtenerToken() }
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
//Bueno este es el archivo main, el que hace correr la app
//tambien se crean los objetos viewModel, asi se crean apenas se corre la app y gracias a que
//Navegacion las definimos como parametros se les pasan a las pantallas por ahi
