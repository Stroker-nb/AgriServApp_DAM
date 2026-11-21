package com.duoc.agriserv.viewModel

/**
 * Estado de la pantalla de solicitudes del productor: si está cargando, si hubo un error y la
 * lista ya con los textos listos para mostrar.
 */
data class SolicitanteUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val solicitudes: List<SolicitudItem> = emptyList()
)

// Una solicitud lista para dibujarse en una tarjeta
data class SolicitudItem(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val estado: String,
    val fotoUrl: String?
)