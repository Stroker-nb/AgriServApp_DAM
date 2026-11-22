package com.duoc.agriserv.viewModel

/**
 * Estado de la pantalla "Mis ofertas": si está cargando, si hubo un error y la lista ya con los
 * textos listos para mostrar.
 */
data class MisOfertasUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val ofertas: List<OfertaItem> = emptyList()
)

// Una oferta lista para dibujarse en una tarjeta de lista
data class OfertaItem(
    val id: Int,
    val solicitudId: Int,
    val titulo: String,
    val detalle: String,
    val precio: String,
    val estado: String,
    val comentario: String?,
    val fotoUrl: String?
)