package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.Obstaculo
import com.duoc.agriserv.model.TipoProducto
import android.net.Uri
/**
 * Estado de la pantalla "Nueva solicitud": lo que ha elegido o escrito el usuario, el error de
 * cada campo y el resultado del envío. Las opciones sin elegir se guardan como null.
 */
data class NuevaSolicitudUiState(
    val tipoProducto: TipoProducto? = null,
    val superficie: String = "",
    val tieneAgua: Boolean? = null,
    val aplicacion: Aplicacion? = null,
    val obstaculos: Set<Obstaculo> = emptySet(),
    val comentario: String = "",
    val zona: String = "",
    val errorTipoProducto: String? = null,
    val errorSuperficie: String? = null,
    val errorTieneAgua: String? = null,
    val errorAplicacion: String? = null,
    val errorComentario: String? = null,
    val enviando: Boolean = false,
    val errorServidor: String? = null,
    val solicitudCreada: Boolean = false,

    val latitud: Double? = null,
    val longitud: Double? = null,
    val errorUbicacion: String? = null,
    val fotoUri: Uri? = null,
    val errorFoto: String? = null,
) {
    val hayErrores: Boolean
        get() = listOf(
            errorTipoProducto, errorSuperficie, errorTieneAgua, errorAplicacion,
            errorComentario, errorUbicacion
        ).any { it != null }
}