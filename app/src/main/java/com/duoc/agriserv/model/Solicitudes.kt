package com.duoc.agriserv.model

data class Solicitudes (
    val idSolicitud : Int,
    val usuario : Solicitante,
    val disAgua : Boolean,
    val aplicacion : String,
    val obstaculos : MutableList<String>,
    val comentario : String
){

}