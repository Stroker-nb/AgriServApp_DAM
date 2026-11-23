package com.duoc.agriserv.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.agriserv.model.NuevaOferta
import com.duoc.agriserv.repository.OfertaRepository
import com.duoc.agriserv.repository.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Lógica de la pantalla "Enviar oferta": valida el precio y el comentario y envía la oferta
 * a la API a través del repositorio.
 */
class NuevaOfertaScreenViewModel(private val repositorio: OfertaRepository) : ViewModel() {

    private val _estado = MutableStateFlow(NuevaOfertaUiState())
    val estado: StateFlow<NuevaOfertaUiState> = _estado.asStateFlow()

    fun onPrecioChange(valor: String) = _estado.update {
        it.copy(precio = valor, errorPrecio = Validaciones.validarPrecio(valor))
    }

    fun onComentarioChange(valor: String) = _estado.update {
        it.copy(comentario = valor, errorComentario = Validaciones.validarComentario(valor))
    }

    // Valida los campos y, si no hay errores, envía la oferta sobre la solicitud indicada
    fun enviar(idSolicitud: Int) {
        val actual = _estado.value
        val errorPrecio = Validaciones.validarPrecio(actual.precio)
        val errorComentario = Validaciones.validarComentario(actual.comentario)

        _estado.update {
            it.copy(errorPrecio = errorPrecio, errorComentario = errorComentario, errorServidor = null)
        }
        if (errorPrecio != null || errorComentario != null) return

        viewModelScope.launch {
            _estado.update { it.copy(enviando = true) }
            try {
                repositorio.crearOferta(
                    NuevaOferta(
                        solicitudId = idSolicitud,
                        precio = actual.precio.toInt(),
                        comentario = actual.comentario.trim()
                    )
                )
                _estado.update { it.copy(enviando = false, ofertaCreada = true) }
            } catch (e: HttpException) {
                Log.e("AgriServ", "Error al enviar la oferta", e)
                _estado.update {
                    it.copy(
                        enviando = false,
                        errorServidor = "No se pudo enviar la oferta. Puede que ya hayas ofertado en esta solicitud o que ya no esté disponible"
                    )
                }
            } catch (e: Exception) {
                Log.e("AgriServ", "Error al enviar la oferta", e)
                _estado.update {
                    it.copy(enviando = false, errorServidor = "No se pudo conectar con el servidor. Inténtalo de nuevo")
                }
            }
        }
    }

    // Deja el formulario limpio para una nueva oferta
    fun reiniciar() {
        _estado.value = NuevaOfertaUiState()
    }
}