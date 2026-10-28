package com.duoc.agriserv.model

data class Solicitudes(
    val idSolicitud: Int,
    val usuario: Usuario,
    val disAgua: Boolean,
    val aplicacion: String,
    val obstaculos: MutableList<String>,
    val comentario: String,
    val superficie: Double,
    val tipoProducto: String,
    val fecha: String,
    val estado: EstadoSolicitud = EstadoSolicitud.Publicando
)

sealed class EstadoSolicitud {
    data object Publicando: EstadoSolicitud()
    data object RecibiendoOfertas : EstadoSolicitud()
    data object EnTrabajo : EstadoSolicitud()
    data object Terminada : EstadoSolicitud()
}