package com.duoc.agriserv.model

data class Ofertas(
    val idOferta: Int,
    val usuario: Usuario,
    val solicitud: Solicitudes,
    val precio: Int,
    val descripcion : String,
    val fecha: String,
    val estado: EstadoOferta = EstadoOferta.Pendiente
){}
sealed class EstadoOferta {
    data object Pendiente : EstadoOferta()
    data object Aceptada : EstadoOferta()
    data object Rechazada : EstadoOferta()
}

