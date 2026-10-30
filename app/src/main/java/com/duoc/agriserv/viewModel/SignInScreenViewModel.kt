package com.duoc.agriserv.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInScreenViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(SignInUiState())
    val estado: StateFlow<SignInUiState> = _estado.asStateFlow()

    // Cada campo se valida al escribir, así el error aparece solo cuando el usuario ya interactuó
    fun onNombreChange(valor: String) = _estado.update {
        it.copy(nombre = valor, errorNombre = Validaciones.validarNombrePropio(valor, "nombre"))
    }

    fun onApellidoChange(valor: String) = _estado.update {
        it.copy(apellido = valor, errorApellido = Validaciones.validarNombrePropio(valor, "apellido"))
    }

    fun onRutChange(valor: String) = _estado.update {
        it.copy(rut = valor, errorRut = Validaciones.validarRut(valor))
    }

    fun onCorreoChange(valor: String) = _estado.update {
        it.copy(correo = valor, errorCorreo = Validaciones.validarCorreo(valor))
    }

    fun onContrasenaChange(valor: String) = _estado.update {
        it.copy(contrasena = valor, errorContrasena = Validaciones.validarContrasena(valor))
    }

    fun onRolChange(rol: Rol) = _estado.update { it.copy(rol = rol) }

    fun registrar() {
        val actual = _estado.value

        // Al pulsar el botón se validan todos los campos, incluso los que no se tocaron
        val validado = actual.copy(
            errorNombre = Validaciones.validarNombrePropio(actual.nombre, "nombre"),
            errorApellido = Validaciones.validarNombrePropio(actual.apellido, "apellido"),
            errorRut = Validaciones.validarRut(actual.rut),
            errorCorreo = Validaciones.validarCorreo(actual.correo),
            errorContrasena = Validaciones.validarContrasena(actual.contrasena)
        )
        _estado.value = validado
        if (validado.hayErrores) return

        viewModelScope.launch {
            val correoNormalizado = actual.correo.trim().lowercase()

            if (repositorio.buscarPorCorreo(correoNormalizado) != null) {
                _estado.update { it.copy(errorCorreo = "Este correo ya está registrado") }
                return@launch
            }

            repositorio.registrar(
                Usuario(
                    nombre = actual.nombre.trim(),
                    apellido = actual.apellido.trim(),
                    rut = actual.rut.trim(),
                    correo = correoNormalizado,
                    contrasena = actual.contrasena,
                    rol = actual.rol
                )
            )
            _estado.update { it.copy(registroExitoso = true) }
        }
    }

    // Deja el formulario limpio para un nuevo registro
    fun reiniciarFormulario() {
        _estado.value = SignInUiState()
    }
}

//QUE HACE ESTA CLASE?
//En esta clase vemos toda la logica detras de SingInScreen
//Trabaja junto a validaciones para validar que los datos en los campos del Screene esten correctos
//Y trabaja junto a Usuario repository para guardar al usuarior que se esta registrandos