package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.EstadoSolicitud
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.model.TipoProducto
import com.duoc.agriserv.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de la pantalla de inicio del productor: pide sus solicitudes a la API y las convierte
 * en textos listos para mostrar, para que la pantalla solo las dibuje.
 */
class SolicitanteScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    private val _estado = MutableStateFlow(SolicitanteUiState())
    val solicitudes = repositorio.obtenerMisSolicitudes().map { it.aItem(repositorio.direccionImagen(it.foto)) }

    fun cargarSolicitudes() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            try {
                val solicitudes = repositorio.obtenerMisSolicitudes().map { convertirEnItem(it) }
                _estado.update { it.copy(cargando = false, solicitudes = solicitudes) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar las solicitudes", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar tus solicitudes. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }

}