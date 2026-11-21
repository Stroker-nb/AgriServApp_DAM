package com.duoc.agriserv.model

import com.google.gson.annotations.SerializedName

/**
 * Solicitud de aplicación agrícola tal como la entrega la API de Xano.
 * Los nombres con @SerializedName son los de las columnas de la tabla solicitud.
 */
data class Solicitud(
    val id: Int = 0,
    @SerializedName("created_at") val creadaEn: Long = 0,
    @SerializedName("tipo_producto") val tipoProducto: TipoProducto,
    val superficie: Double = 0.0,
    @SerializedName("tiene_agua") val tieneAgua: Boolean = false,
    val aplicacion: Aplicacion,
    val obstaculos: String? = null,
    val comentario: String? = null,
    val foto: Imagen? = null,
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val zona: String? = null,
    val estado: EstadoSolicitud,
    @SerializedName("oferta_aceptada_id") val ofertaAceptadaId: Int? = null,
    val solicitante: UsuarioPublico? = null
)

// Tipo de producto que se aplicará en el terreno
enum class TipoProducto(val etiqueta: String) {
    GRANULADO("Granulado"),
    LIQUIDO("Líquido")
}

// Momento del día en que se hará la aplicación
enum class Aplicacion(val etiqueta: String) {
    DIURNA("Diurna"),
    NOCTURNA("Nocturna")
}

// Etapas de una solicitud: publicada, con una oferta aceptada, o finalizada
enum class EstadoSolicitud(val etiqueta: String) {
    PUBLICADA("Publicada"),
    ACEPTADA("Con oferta aceptada"),
    FINALIZADA("Finalizada")
}

// Obstáculos que puede haber en el terreno; la API los guarda como texto separado por comas
enum class Obstaculo(val etiqueta: String) {
    ANTENAS("Antenas"),
    CABLES("Cables"),
    TORRES("Torres de alta tensión"),
    OTROS("Otros")
}