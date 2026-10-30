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

//QUE HACE ESTA CLASE?
//Esta clase representa los datos de las ofertas dentro de la app,
// tiene una sealed class con el estado de estas