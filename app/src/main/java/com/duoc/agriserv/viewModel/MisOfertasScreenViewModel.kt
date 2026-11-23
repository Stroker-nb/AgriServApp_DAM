package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Oferta
import com.duoc.agriserv.repository.OfertaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de la pantalla "Mis ofertas": pide a la API las ofertas del aplicador y las convierte
 * en textos listos para mostrar.
 */
class MisOfertasScreenViewModel(private val repositorio: OfertaRepository) : ViewModel() {

    private val _estado = MutableStateFlow(MisOfertasUiState())
    val estado: StateFlow<MisOfertasUiState> = _estado.asStateFlow()

    fun cargarOfertas() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            try {
                val ofertas = repositorio.obtenerMisOfertas().map { convertirEnItem(it) }
                _estado.update { it.copy(cargando = false, ofertas = ofertas) }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al cargar mis ofertas", e)
                _estado.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron cargar tus ofertas. Revisa tu conexión e inténtalo de nuevo"
                    )
                }
            }
        }
    }

    private fun convertirEnItem(oferta: Oferta): OfertaItem {
        val item = oferta.solicitud?.aItem(repositorio.direccionImagen(oferta.solicitud.foto))
        return OfertaItem(
            id = oferta.id,
            solicitudId = oferta.solicitudId,
            titulo = item?.titulo ?: "Solicitud #${oferta.solicitudId}",
            detalle = item?.detalle ?: "",
            precio = oferta.precio.comoPesos(),
            estado = oferta.estado.etiqueta,
            comentario = oferta.comentario?.takeIf { it.isNotBlank() },
            fotoUrl = item?.fotoUrl
        )
    }
}