package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Solicitud

// Una solicitud lista para dibujarse en una tarjeta de lista
data class SolicitudItem(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val estado: String,
    val fotoUrl: String?
)

// Convierte una solicitud de la API en los textos que muestra la tarjeta
fun Solicitud.aItem(fotoUrl: String?): SolicitudItem {
    val zonaTexto = zona?.takeIf { it.isNotBlank() }
    val momento = aplicacion.etiqueta

    return SolicitudItem(
        id = id,
        titulo = "${tipoProducto.etiqueta} · ${"%.1f".format(superficie)} hectáreas",
        detalle = if (zonaTexto != null) "$momento · $zonaTexto" else momento,
        estado = estado.etiqueta,
        fotoUrl = fotoUrl
    )
}