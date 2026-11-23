package com.duoc.agriserv.remote

import com.duoc.agriserv.model.RespuestaEliminacion
import com.duoc.agriserv.model.Solicitud
import com.duoc.agriserv.model.UsuarioPublico
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

// Endpoints de solicitudes del grupo de API de Xano
interface SolicitudApi {

    @GET("mis_solicitudes")
    suspend fun obtenerMisSolicitudes(): List<Solicitud>

    // Los datos van como campos de texto y, si hay, la foto como archivo adjunto
    @Multipart
    @POST("solicitud")
    suspend fun crearSolicitud(
        @PartMap campos: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part foto: MultipartBody.Part?
    ): Solicitud

    // Solicitudes publicadas de todos los productores, para que los aplicadores las vean
    @GET("solicitudes_abiertas")
    suspend fun obtenerSolicitudesAbiertas(): List<Solicitud>

    @GET("solicitud_detalle/{id}")
    suspend fun obtenerSolicitud(@Path("id") id: Int): Solicitud

    @DELETE("solicitud/{id}")
    suspend fun eliminarSolicitud(@Path("id") id: Int): RespuestaEliminacion

    @GET("perfil_publico/{id}")
    suspend fun obtenerPerfilPublico(@Path("id") id: Int): UsuarioPublico
}