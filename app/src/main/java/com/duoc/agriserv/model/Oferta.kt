package com.duoc.agriserv.model

import com.google.gson.annotations.SerializedName

/**
 * Oferta de un aplicador sobre una solicitud, tal como la entrega la API de Xano.
 * La solicitud (en "Mis ofertas") o el ofertante (en las ofertas de una solicitud) vienen anidados.
 */
data class Oferta(
    val id: Int = 0,
    @SerializedName("created_at") val creadaEn: Long = 0,
    @SerializedName("solicitud_id") val solicitudId: Int = 0,
    val precio: Int = 0,
    val comentario: String? = null,
    val estado: EstadoOferta = EstadoOferta.PENDIENTE,
    val solicitud: Solicitud? = null,
    val ofertante: UsuarioPublico? = null
)

// Etapas de una oferta: esperando respuesta, elegida por el productor, o descartada
enum class EstadoOferta(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada")
}

// Datos que se envían a la API para ofertar. El ofertante y el estado los asigna Xano.
data class NuevaOferta(
    @SerializedName("solicitud_id") val solicitudId: Int,
    val precio: Int,
    val comentario: String
)