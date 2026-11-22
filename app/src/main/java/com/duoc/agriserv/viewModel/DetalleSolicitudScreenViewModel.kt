package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.EstadoSolicitud
import com.duoc.agriserv.model.Obstaculo
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de la pantalla de detalle de una solicitud: la pide a la API por su id, la convierte en
 * textos listos para mostrar y permite eliminarla.
 */
class DetalleSolicitudScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    private val _estado = MutableStateFlow(DetalleSolicitudUiState())
    val estado: StateFlow<DetalleSolicitudUiState> = _estado.asStateFlow()

    fun cargarSolicitud(id: Int) {
        // Se limpia primero para no mostrar un instante los datos de la solicitud anterior
        _estado.value = DetalleSolicitudUiState()

        viewModelScope.launch {
            try {
                val solicitud = repositorio.obtenerSolicitud(id)
                _estado.update { it.copy(cargando = false, solicitud = convertirEnDetalle(solicitud)) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar la solicitud", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudo cargar la solicitud. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }

    fun eliminarSolicitud(id: Int) {
        viewModelScope.launch {
            _estado.update { it.copy(eliminando = true, errorEliminar = null) }
            try {
                repositorio.eliminarSolicitud(id)
                _estado.update { it.copy(eliminando = false, solicitudEliminada = true) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al eliminar la solicitud", e)
                _estado.update {
                    it.copy(
                        eliminando = false,
                        errorEliminar = "No se pudo eliminar la solicitud. Inténtalo de nuevo"
                    )
                }
            }
        }
    }

    private fun convertirEnDetalle(solicitud: Solicitud): SolicitudDetalle {
        val obstaculos = solicitud.obstaculos.orEmpty()
            .split(",")
            .mapNotNull { nombre -> Obstaculo.entries.find { it.name == nombre.trim() }?.etiqueta }

        return SolicitudDetalle(
            titulo = "${solicitud.tipoProducto.etiqueta} · ${"%.1f".format(solicitud.superficie)} hectáreas",
            producto = solicitud.tipoProducto.etiqueta,
            superficie = "${"%.1f".format(solicitud.superficie)} hectáreas",
            agua = if (solicitud.tieneAgua) "Sí" else "No",
            aplicacion = solicitud.aplicacion.etiqueta,
            obstaculos = if (obstaculos.isEmpty()) "Sin obstáculos" else obstaculos.joinToString(", "),
            zona = solicitud.zona?.takeIf { it.isNotBlank() } ?: "No indicada",
            estado = solicitud.estado.etiqueta,
            comentario = solicitud.comentario?.takeIf { it.isNotBlank() } ?: "Sin comentarios",
            fotoUrl = repositorio.direccionImagen(solicitud.foto),
            latitud = solicitud.latitud,
            longitud = solicitud.longitud,
            solicitanteNombre = solicitud.solicitante
                ?.let { "${it.nombre} ${it.apellido}" } ?: "Productor",
            solicitanteFotoUrl = repositorio.direccionImagen(solicitud.solicitante?.foto),
            sePuedeEliminar = solicitud.estado == EstadoSolicitud.PUBLICADA
        )
    }
}