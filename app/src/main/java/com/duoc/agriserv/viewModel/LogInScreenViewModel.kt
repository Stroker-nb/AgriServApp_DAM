package com.duoc.agriserv.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

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

            _estado.update { it.copy(usuarioLogueado = usuario) }
        }
    }

    // Limpia el formulario; se usará al salir de la pantalla de login hacia el home
    fun reiniciarFormulario() {
        _estado.value = LoginUiState()
    }
}