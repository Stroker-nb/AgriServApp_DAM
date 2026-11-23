package com.duoc.agriserv.remote

import com.duoc.agriserv.model.NuevaOferta
import com.duoc.agriserv.model.Oferta
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Endpoints de ofertas del grupo de API de Xano
interface OfertaApi {

    @POST("oferta")
    suspend fun crearOferta(@Body oferta: NuevaOferta): Oferta

    @GET("mis_ofertas")
    suspend fun obtenerMisOfertas(): List<Oferta>

    @GET("ofertas_de_solicitud/{id}")
    suspend fun obtenerOfertasDeSolicitud(@Path("id") id: Int): List<Oferta>

    // Acepta la oferta y rechaza las demás de la misma solicitud; la respuesta no se usa
    @POST("aceptar_oferta/{id}")
    suspend fun aceptarOferta(@Path("id") id: Int)
}