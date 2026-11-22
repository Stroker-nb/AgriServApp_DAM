package com.duoc.agriserv.viewModel

/**
 * Estado de la pantalla de detalle de una solicitud: si está cargando, si hubo un error, los datos
 * ya con los textos listos para mostrar y el resultado de eliminarla.
 */
data class DetalleSolicitudUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val solicitud: SolicitudDetalle? = null,
    val eliminando: Boolean = false,
    val errorEliminar: String? = null,
    val solicitudEliminada: Boolean = false
)

// Los datos de una solicitud ya convertidos en textos para la pantalla de detalle
data class SolicitudDetalle(
    val titulo: String,
    val producto: String,
    val superficie: String,
    val agua: String,
    val aplicacion: String,
    val obstaculos: String,
    val zona: String,
    val estado: String,
    val comentario: String,
    val fotoUrl: String?,
    val latitud: Double,
    val longitud: Double,
    val solicitanteNombre: String,
    val solicitanteFotoUrl: String?,
    // Una solicitud solo se puede eliminar mientras está publicada
    val sePuedeEliminar: Boolean
)