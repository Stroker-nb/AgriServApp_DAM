package com.duoc.agriserv.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.repository.UsuarioRepository
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
            val usuario = repositorio.buscarPorCorreo(actual.correo.trim().lowercase())

            // Mismo mensaje si el correo no existe o la contraseña no coincide,
            // para no revelar cuál de los dos falló
            if (usuario == null || usuario.contrasena != actual.contrasena) {
                _estado.update { it.copy(errorCredenciales = "Correo o contraseña incorrectos") }
                return@launch
            }

            // Se guarda la sesión en Room para que sobreviva al cierre de la app
            repositorio.iniciarSesion(usuario.idUsuario)
            _estado.update { it.copy(usuarioLogueado = usuario, sesion = usuario) }
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