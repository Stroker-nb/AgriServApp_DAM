package com.duoc.agriserv.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.model.TipoProducto
import com.duoc.agriserv.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de la pantalla de inicio del aplicador: pide a la API las solicitudes publicadas, calcula
 * la distancia hasta el aplicador, descarta las que están fuera del radio, las ordena de la más
 * cercana a la más lejana y aplica los filtros opcionales.
 */
class OfertanteScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    companion object {
        // Radio fijo: solo se muestran solicitudes a menos de esta distancia (en km)
        const val RADIO_KM = 50.0
    }

    private val _estado = MutableStateFlow(OfertanteUiState())
    val estado: StateFlow<OfertanteUiState> = _estado.asStateFlow()

    // Lo que devolvió la API, sin filtrar; los filtros se aplican sobre esta lista
    private var abiertas: List<Solicitud> = emptyList()
    private var cargada = false
    private var ubicacion: Pair<Double, Double>? = null

    fun cargarSolicitudes() {
        viewModelScope.launch {
            cargada = false
            _estado.update { it.copy(cargando = true, error = null) }
            try {
                abiertas = repositorio.obtenerSolicitudesAbiertas()
                cargada = true
                recalcular()
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

    // La ubicación (aproximada) del aplicador llegó: se activa el radio y el orden por cercanía
    fun onUbicacionObtenida(latitud: Double, longitud: Double) {
        ubicacion = latitud to longitud
        _estado.update { it.copy(ubicacionConocida = true, avisoUbicacion = null) }
        recalcular()
    }

    // No hay permiso o no se pudo obtener la ubicación: se muestran todas, sin orden de distancia
    fun onUbicacionNoDisponible(aviso: String) {
        ubicacion = null
        _estado.update { it.copy(ubicacionConocida = false, avisoUbicacion = aviso) }
        recalcular()
    }

    fun aplicarFiltros(tipoProducto: TipoProducto?, aplicacion: Aplicacion?, superficieMinima: String) {
        _estado.update {
            it.copy(
                filtroTipoProducto = tipoProducto,
                filtroAplicacion = aplicacion,
                filtroSuperficieMinima = superficieMinima.trim()
            )
        }
        recalcular()
    }

    fun limpiarFiltros() = aplicarFiltros(null, null, "")

    // Filtra, ordena y convierte las solicitudes en los textos que dibuja la pantalla
    private fun recalcular() {
        if (!cargada) return
        val actual = _estado.value
        val superficieMinima = actual.filtroSuperficieMinima.replace(',', '.').toDoubleOrNull()

        val solicitudes = abiertas
            .map { it to distanciaKm(it) }
            .filter { (_, distancia) -> distancia == null || distancia <= RADIO_KM }
            .filter { (s, _) -> actual.filtroTipoProducto == null || s.tipoProducto == actual.filtroTipoProducto }
            .filter { (s, _) -> actual.filtroAplicacion == null || s.aplicacion == actual.filtroAplicacion }
            .filter { (s, _) -> superficieMinima == null || s.superficie >= superficieMinima }
            .sortedBy { (_, distancia) -> distancia ?: Double.MAX_VALUE }
            .map { (s, distancia) -> s.aItem(repositorio.direccionImagen(s.foto), distancia) }

        _estado.update { it.copy(cargando = false, solicitudes = solicitudes) }
    }

    // Distancia en km entre el aplicador y el terreno; null si no se conoce la ubicación del aplicador
    private fun distanciaKm(solicitud: Solicitud): Double? {
        val (latitud, longitud) = ubicacion ?: return null
        val resultado = FloatArray(1)
        Location.distanceBetween(latitud, longitud, solicitud.latitud, solicitud.longitud, resultado)
        return resultado[0] / 1000.0
    }
}