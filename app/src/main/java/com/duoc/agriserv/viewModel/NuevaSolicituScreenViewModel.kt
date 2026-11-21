package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.NuevaSolicitud
import com.duoc.agriserv.model.Obstaculo
import com.duoc.agriserv.model.TipoProducto
import com.duoc.agriserv.repository.SolicitudRepository
import com.duoc.agriserv.repository.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Lógica de la pantalla "Nueva solicitud": valida cada campo mientras se completa y todos juntos
 * al publicar, y envía la solicitud a la API a través del repositorio.
 */
class NuevaSolicitudScreenViewModel(private val repositorio: SolicitudRepository) : ViewModel() {

    private val _estado = MutableStateFlow(NuevaSolicitudUiState())
    val estado: StateFlow<NuevaSolicitudUiState> = _estado.asStateFlow()

    fun onTipoProductoChange(valor: TipoProducto) = _estado.update {
        it.copy(tipoProducto = valor, errorTipoProducto = null)
    }

    fun onSuperficieChange(valor: String) = _estado.update {
        it.copy(superficie = valor, errorSuperficie = Validaciones.validarSuperficie(valor))
    }

    fun onTieneAguaChange(valor: Boolean) = _estado.update {
        it.copy(tieneAgua = valor, errorTieneAgua = null)
    }

    fun onAplicacionChange(valor: Aplicacion) = _estado.update {
        it.copy(aplicacion = valor, errorAplicacion = null)
    }

    // Marca o desmarca un obstáculo de la lista
    fun onObstaculoChange(obstaculo: Obstaculo, marcado: Boolean) = _estado.update {
        it.copy(obstaculos = if (marcado) it.obstaculos + obstaculo else it.obstaculos - obstaculo)
    }

    fun onComentarioChange(valor: String) = _estado.update {
        it.copy(comentario = valor, errorComentario = Validaciones.validarComentario(valor))
    }

    fun onZonaChange(valor: String) = _estado.update { it.copy(zona = valor) }

    // Valida todos los campos y, si no hay errores, publica la solicitud en la API
    fun publicar() {
        val actual = _estado.value

        val validado = actual.copy(
            errorTipoProducto = Validaciones.validarSeleccion(actual.tipoProducto, "el tipo de producto"),
            errorSuperficie = Validaciones.validarSuperficie(actual.superficie),
            errorTieneAgua = Validaciones.validarSeleccion(actual.tieneAgua, "si el terreno tiene agua"),
            errorAplicacion = Validaciones.validarSeleccion(actual.aplicacion, "el momento de la aplicación"),
            errorComentario = Validaciones.validarComentario(actual.comentario),
            errorServidor = null
        )
        _estado.value = validado
        if (validado.hayErrores) return

        val tipoProducto = actual.tipoProducto ?: return
        val tieneAgua = actual.tieneAgua ?: return
        val aplicacion = actual.aplicacion ?: return

        viewModelScope.launch {
            _estado.update { it.copy(enviando = true) }
            try {
                repositorio.crearSolicitud(
                    NuevaSolicitud(
                        tipoProducto = tipoProducto,
                        superficie = actual.superficie.replace(',', '.').toDouble(),
                        tieneAgua = tieneAgua,
                        aplicacion = aplicacion,
                        obstaculos = actual.obstaculos.joinToString(",") { it.name },
                        comentario = actual.comentario.trim(),
                        latitud = LATITUD_PROVISORIA,
                        longitud = LONGITUD_PROVISORIA,
                        zona = actual.zona.trim()
                    )
                )
                _estado.update { it.copy(enviando = false, solicitudCreada = true) }
            } catch (e: HttpException) {
                Log.e("AgriServ", "Error al publicar la solicitud", e)
                _estado.update {
                    it.copy(enviando = false, errorServidor = "No se pudo publicar la solicitud. Revisa los datos e inténtalo de nuevo")
                }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al publicar la solicitud", e)
                _estado.update {
                    it.copy(enviando = false, errorServidor = "No se pudo conectar con el servidor. Inténtalo de nuevo")
                }
            }
        }
    }

    // Deja el formulario limpio para una nueva solicitud
    fun reiniciarFormulario() {
        _estado.value = NuevaSolicitudUiState()
    }

    private companion object {
        // Ubicación provisoria (Santiago): se reemplaza por la ubicación real en la Fase 1E
        const val LATITUD_PROVISORIA = -33.45
        const val LONGITUD_PROVISORIA = -70.66
    }
}