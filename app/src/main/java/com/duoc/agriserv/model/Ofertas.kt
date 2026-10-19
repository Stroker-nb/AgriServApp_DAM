package com.duoc.agriserv.model

data class Ofertas (
    val idOferta : Int,
    val ofertante : Ofertante,
    val solicitud : Solicitudes,
    val precio : Int
) {
}