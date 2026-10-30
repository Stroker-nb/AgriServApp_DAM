package com.duoc.agriserv.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    private val _estado = MutableStateFlow(ProfileUiState())
    val estado: StateFlow<ProfileUiState> = _estado.asStateFlow()

    // Lee de Room al usuario con la sesión activa y prepara los datos para la pantalla
    fun cargarPerfil() {
        // Se limpia primero para no mostrar un instante los datos de la sesión anterior
        _estado.value = ProfileUiState()

        viewModelScope.launch {
            val usuario = repositorio.obtenerUsuarioConSesion() ?: return@launch

            _estado.update {
                it.copy(
                    cargado = true,
                    nombreCompleto = "${usuario.nombre} ${usuario.apellido}",
                    tipoCuenta = if (usuario.rol == Rol.SOLICITANTE) "Productor" else "Aplicador",
                    nombre = usuario.nombre,
                    apellido = usuario.apellido,
                    correo = usuario.correo,
                    rut = usuario.rut,
                    esOfertante = usuario.rol == Rol.OFERTANTE,
                    calificacion = usuario.calificacion
                )
            }
        }
    }

    fun onPermisoCamaraDenegado() = _estado.update { it.copy(permisoCamaraDenegado = true) }

    fun cerrarMensajePermiso() = _estado.update { it.copy(permisoCamaraDenegado = false) }
}