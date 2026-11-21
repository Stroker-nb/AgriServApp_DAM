package com.duoc.agriserv.remote

import com.duoc.agriserv.model.Solicitud
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

// Endpoints de solicitudes del grupo de API de Xano
interface SolicitudApi {

    @GET("solicitud/mias")
    suspend fun obtenerMisSolicitudes(): List<Solicitud>

    // Los datos van como campos de texto y, si hay, la foto como archivo adjunto
    @Multipart
    @POST("solicitud")
    suspend fun crearSolicitud(
        @PartMap campos: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part foto: MultipartBody.Part?
    ): Solicitud
}