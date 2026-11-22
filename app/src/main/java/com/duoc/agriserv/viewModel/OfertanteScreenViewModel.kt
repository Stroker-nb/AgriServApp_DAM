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
 * Lógica de la pantalla de inicio del aplicador: pide a la API las solicitudes publicadas y las
 * convierte en textos listos para mostrar. Todavía sin distancia ni filtros.
 */
class OfertanteScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    private val _estado = MutableStateFlow(OfertanteUiState())
    val estado: StateFlow<OfertanteUiState> = _estado.asStateFlow()

    fun cargarSolicitudes() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            try {
                val solicitudes = repositorio.obtenerSolicitudesAbiertas()
                    .map { it.aItem(repositorio.direccionImagen(it.foto)) }
                _estado.update { it.copy(cargando = false, solicitudes = solicitudes) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar las solicitudes abiertas", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar las solicitudes. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }
}