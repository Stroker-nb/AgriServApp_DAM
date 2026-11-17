package com.duoc.agriserv.remote

import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.RespuestaToken
import com.duoc.agriserv.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Endpoints de autenticación del grupo de API de Xano
interface ApiService {

    @POST("auth/signup")
    suspend fun registrarUsuario(@Body nuevoUsuario: NuevoUsuario): RespuestaToken

    @POST("auth/login")
    suspend fun iniciarSesion(@Body credenciales: Credenciales): RespuestaToken

    @GET("auth/me")
    suspend fun obtenerUsuarioActual(@Header("Authorization") token: String): Usuario
}