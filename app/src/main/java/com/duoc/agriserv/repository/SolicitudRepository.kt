package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevaSolicitud
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.remote.RetrofitInstance
import com.duoc.agriserv.remote.SolicitudApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
// Contrato del repositorio de solicitudes: los ViewModel dependen de esta interfaz, así las
// pruebas pueden entregarles un repositorio falso
interface SolicitudRepository {
    suspend fun obtenerMisSolicitudes(): List<Solicitud>
    suspend fun obtenerSolicitudesAbiertas(): List<Solicitud>
    suspend fun obtenerSolicitud(id: Int): Solicitud
    suspend fun crearSolicitud(solicitud: NuevaSolicitud): Solicitud
    suspend fun eliminarSolicitud(id: Int)
    fun direccionImagen(imagen: Imagen?): String?
}

/**
 * Intermediario entre los ViewModel y la API de Xano para todo lo relacionado con solicitudes.
 * El token de la sesión se envía solo en cada petición.
 */
class SolicitudRepositoryImpl(private val api: SolicitudApi) : SolicitudRepository {

    // Solicitudes creadas por el usuario con la sesión abierta, de la más nueva a la más antigua
    override suspend fun obtenerMisSolicitudes(): List<Solicitud> = api.obtenerMisSolicitudes()

    // Solicitudes publicadas de todos los productores
    override suspend fun obtenerSolicitudesAbiertas(): List<Solicitud> = api.obtenerSolicitudesAbiertas()

    override suspend fun obtenerSolicitud(id: Int): Solicitud = api.obtenerSolicitud(id)

    // Publica una solicitud en Xano; los campos opcionales se envían solo si tienen contenido
    override suspend fun crearSolicitud(solicitud: NuevaSolicitud): Solicitud {
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

        val parteFoto = solicitud.foto?.let { bytes ->
            val extension = when (solicitud.fotoTipoMime) {
                "image/png" -> "png"
                "image/webp" -> "webp"
                else -> "jpg"
            }
            MultipartBody.Part.createFormData(
                "foto",
                "solicitud.$extension",
                bytes.toRequestBody(solicitud.fotoTipoMime.toMediaType())
            )
        }
        return api.crearSolicitud(campos, parteFoto)
    }

    // Solo el dueño puede eliminar una solicitud publicada; si no, la API responde con un error HTTP
    override suspend fun eliminarSolicitud(id: Int) {
        api.eliminarSolicitud(id)
    }

    override fun direccionImagen(imagen: Imagen?): String? = RetrofitInstance.direccionImagen(imagen)

    // Convierte un texto en un campo de texto de la petición multipart
    private fun String.comoTexto(): RequestBody =
        toRequestBody("text/plain; charset=utf-8".toMediaType())
}