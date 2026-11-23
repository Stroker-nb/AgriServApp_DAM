package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica del perfil público: pide a la API los datos visibles de un usuario por su id.
 */
class PerfilPublicoScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    private val _estado = MutableStateFlow(PerfilPublicoUiState())
    val estado: StateFlow<PerfilPublicoUiState> = _estado.asStateFlow()

    fun cargarPerfil(id: Int) {
        // Se limpia primero para no mostrar un instante el perfil anterior
        _estado.value = PerfilPublicoUiState()
        viewModelScope.launch {
            try {
                val perfil = repositorio.obtenerPerfilPublico(id)
                _estado.update {
                    it.copy(
                        cargando = false,
                        perfil = perfil,
                        fotoUrl = repositorio.direccionImagen(perfil.foto)
                    )
                }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar el perfil público", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudo cargar el perfil. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }
}