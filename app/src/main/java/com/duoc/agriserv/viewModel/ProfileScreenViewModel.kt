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

    //2 Funciones que trabajan para los permisos de uso de la camara y la subida de archivos
    fun onPermisoDenegado(recurso: String) = _estado.update { it.copy(recursoDenegado = recurso) }

    fun cerrarMensajePermiso() = _estado.update { it.copy(recursoDenegado = null) }



}

//QUE HACE ESTA CLASE?
//En esta clase vemos toda la logica detras de profileScreen, trabaja junto a usuarioRepository
//para obtener los datos del usuario Logueado actualmente en la app