package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.EstadoOferta
import com.duoc.agriserv.model.Oferta
import com.duoc.agriserv.repository.OfertaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de las ofertas recibidas por una solicitud: las pide a la API (ya ordenadas por precio),
 * las convierte en textos listos para mostrar y permite al productor aceptar una.
 */
class OfertasRecibidasScreenViewModel(private val repositorio: OfertaRepository) : ViewModel() {

    private val _estado = MutableStateFlow(OfertasRecibidasUiState())
    val estado: StateFlow<OfertasRecibidasUiState> = _estado.asStateFlow()

    fun cargarOfertas(idSolicitud: Int) {
        // Se limpia primero para no mostrar un instante las ofertas de otra solicitud
        _estado.value = OfertasRecibidasUiState()

        viewModelScope.launch {
            try {
                val ofertas = repositorio.obtenerOfertasDeSolicitud(idSolicitud).map { convertirEnItem(it) }
                _estado.update { it.copy(cargando = false, ofertas = ofertas) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar las ofertas recibidas", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar las ofertas. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }

    // La API acepta esta oferta y rechaza las demás en una sola operación
    fun aceptarOferta(idOferta: Int) {
        viewModelScope.launch {
            _estado.update { it.copy(aceptando = true, errorAceptar = null) }
            try {
                repositorio.aceptarOferta(idOferta)
                _estado.update { it.copy(aceptando = false, ofertaAceptada = true) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al aceptar la oferta", e)
                _estado.update {
                    it.copy(
                        aceptando = false,
                        errorAceptar = "No se pudo aceptar la oferta. Inténtalo de nuevo"
                    )
                }
            }
        }
    }

    private fun convertirEnItem(oferta: Oferta): OfertaRecibidaItem {
        val ofertante = oferta.ofertante
        return OfertaRecibidaItem(
            id = oferta.id,
            ofertanteId = ofertante?.id,
            nombre = ofertante?.let { "${it.nombre} ${it.apellido}" } ?: "Aplicador",
            fotoUrl = repositorio.direccionImagen(ofertante?.foto),
            calificacion = ofertante?.calificacion ?: 0.0,
            precio = oferta.precio.comoPesos(),
            comentario = oferta.comentario?.takeIf { it.isNotBlank() },
            estado = oferta.estado.etiqueta,
            esPendiente = oferta.estado == EstadoOferta.PENDIENTE
        )
    }
}