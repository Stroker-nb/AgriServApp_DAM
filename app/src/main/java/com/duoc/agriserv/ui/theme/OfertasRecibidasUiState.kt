package com.duoc.agriserv.viewModel

/**
 * Estado de las ofertas recibidas por una solicitud (vista del productor): si está cargando, si
 * hubo un error, la lista ya con los textos listos y el resultado de aceptar una oferta.
 */
data class OfertasRecibidasUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val ofertas: List<OfertaRecibidaItem> = emptyList(),
    val aceptando: Boolean = false,
    val errorAceptar: String? = null,
    val ofertaAceptada: Boolean = false
)

// Una oferta recibida lista para dibujarse: quién ofertó, cuánto y en qué estado está
data class OfertaRecibidaItem(
    val id: Int,
    val ofertanteId: Int?,
    val nombre: String,
    val fotoUrl: String?,
    val calificacion: Double,
    val precio: String,
    val comentario: String?,
    val estado: String,
    // Solo una oferta pendiente se puede aceptar
    val esPendiente: Boolean
)