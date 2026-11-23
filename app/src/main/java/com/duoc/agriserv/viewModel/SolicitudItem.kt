package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Solicitud

// Una solicitud lista para dibujarse en una tarjeta de lista
data class SolicitudItem(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val estado: String,
    val fotoUrl: String?,
    // Distancia hasta el aplicador en km; null si no se conoce su ubicación
    val distanciaKm: Double? = null
)

// Convierte una solicitud de la API en los textos que muestra la tarjeta
fun Solicitud.aItem(fotoUrl: String?, distanciaKm: Double? = null): SolicitudItem {
    val zonaTexto = zona?.takeIf { it.isNotBlank() }
    val momento = aplicacion.etiqueta
    val distanciaTexto = distanciaKm?.let {
        if (it < 1) "a menos de 1 km" else "a ${"%.0f".format(it)} km"
    }

    return SolicitudItem(
        id = id,
        titulo = "${tipoProducto.etiqueta} · ${"%.1f".format(superficie)} hectáreas",
        detalle = listOfNotNull(momento, zonaTexto, distanciaTexto).joinToString(" · "),
        estado = estado.etiqueta,
        fotoUrl = fotoUrl,
        distanciaKm = distanciaKm
    )
}