package com.duoc.agriserv.model

/**
 * Datos que se envían a la API para publicar una solicitud. El id, el estado y el solicitante
 * los asigna Xano a partir de la sesión abierta.
 */
data class NuevaSolicitud(
    val tipoProducto: TipoProducto,
    val superficie: Double,
    val tieneAgua: Boolean,
    val aplicacion: Aplicacion,
    val obstaculos: String,
    val comentario: String,
    val latitud: Double,
    val longitud: Double,
    val zona: String,
    val foto: ByteArray? = null,
    val fotoTipoMime: String = "image/jpeg"
)