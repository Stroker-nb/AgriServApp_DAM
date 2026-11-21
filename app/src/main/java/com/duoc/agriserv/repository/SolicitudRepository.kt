package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevaSolicitud
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.remote.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Intermediario entre los ViewModel y la API de Xano para todo lo relacionado con solicitudes.
 * El token de la sesión se envía solo en cada petición.
 */
class SolicitudRepository {

    // Solicitudes creadas por el usuario con la sesión abierta, de la más nueva a la más antigua
    suspend fun obtenerMisSolicitudes(): List<Solicitud> =
        RetrofitInstance.solicitudApi.obtenerMisSolicitudes()

    fun direccionImagen(imagen: Imagen?): String? = RetrofitInstance.direccionImagen(imagen)

    // Publica una solicitud en Xano; los campos opcionales se envían solo si tienen contenido
    suspend fun crearSolicitud(solicitud: NuevaSolicitud): Solicitud {
        val campos = mutableMapOf(
            "tipo_producto" to solicitud.tipoProducto.name.comoTexto(),
            "superficie" to solicitud.superficie.toString().comoTexto(),
            "tiene_agua" to solicitud.tieneAgua.toString().comoTexto(),
            "aplicacion" to solicitud.aplicacion.name.comoTexto(),
            "latitud" to solicitud.latitud.toString().comoTexto(),
            "longitud" to solicitud.longitud.toString().comoTexto()
        )
        if (solicitud.obstaculos.isNotBlank()) campos["obstaculos"] = solicitud.obstaculos.comoTexto()
        if (solicitud.comentario.isNotBlank()) campos["comentario"] = solicitud.comentario.comoTexto()
        if (solicitud.zona.isNotBlank()) campos["zona"] = solicitud.zona.comoTexto()

        return RetrofitInstance.solicitudApi.crearSolicitud(campos, null)
    }

    // Convierte un texto en un campo de texto de la petición multipart
    private fun String.comoTexto(): RequestBody =
        toRequestBody("text/plain; charset=utf-8".toMediaType())
}