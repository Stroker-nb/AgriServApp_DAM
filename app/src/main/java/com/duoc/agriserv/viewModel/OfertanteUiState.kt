package com.duoc.agriserv.viewModel

/**
 * Estado de la pantalla de inicio del aplicador: si está cargando, si hubo un error y la lista
 * de solicitudes ya con los textos listos para mostrar.
 */
data class OfertanteUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val solicitudes: List<SolicitudItem> = emptyList()
)