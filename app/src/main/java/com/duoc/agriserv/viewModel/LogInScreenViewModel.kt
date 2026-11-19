package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.repository.UsuarioRepository
import com.duoc.agriserv.repository.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogInScreenViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    // Al abrir la app se revisa si quedó una sesión guardada en Room
    init {
        viewModelScope.launch {
            val usuario = repositorio.obtenerUsuarioConSesion()
            _estado.update { it.copy(sesion = usuario, cargandoSesion = false) }
        }
    }

    fun onCorreoChange(valor: String) = _estado.update {
        it.copy(
            correo = valor,
            errorCorreo = Validaciones.validarCorreo(valor),
            errorCredenciales = null
        )
    }

    fun onContrasenaChange(valor: String) = _estado.update {
        it.copy(
            contrasena = valor,
            errorContrasena = Validaciones.validarContrasena(valor),
            errorCredenciales = null
        )
    }

    fun iniciarSesion() {
        val actual = _estado.value

        // Al pulsar el botón se validan ambos campos, incluso si no se tocaron
        val errorCorreo = Validaciones.validarCorreo(actual.correo)
        val errorContrasena = Validaciones.validarContrasena(actual.contrasena)
        _estado.update {
            it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena)
        }
        if (errorCorreo != null || errorContrasena != null) return

        viewModelScope.launch {
            try {
                val usuario = repositorio.iniciarSesion(
                    actual.correo.trim().lowercase(),
                    actual.contrasena
                )

                if (usuario == null) {
                    _estado.update { it.copy(errorCredenciales = "Correo o contraseña incorrectos") }
                    return@launch
                }

                _estado.update { it.copy(usuarioLogueado = usuario, sesion = usuario) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error de conexión", e)
                _estado.update { it.copy(errorCredenciales = "No se pudo conectar con el servidor. Inténtalo de nuevo") }
            }
        }
    }

    // Limpia el formulario pero conserva la sesión activa
    fun reiniciarFormulario() {
        _estado.update { LoginUiState(sesion = it.sesion, cargandoSesion = false) }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            repositorio.cerrarSesion()
            _estado.value = LoginUiState(cargandoSesion = false)
        }
    }
}

//QUE HACE ESTA CLASE
//En esta clase encontramos toda la logica detras de LogInScreen,
//Trabaja junto a validaciones para ver si los datos ingresados validos para su contexto,
//y trabaja con UsuarioRepository para consultar a la base de datos si el usuario ingresado existep
