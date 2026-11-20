package com.duoc.agriserv.remote

import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.RespuestaToken
import com.duoc.agriserv.model.Usuario
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// Endpoints de autenticación del grupo de API de Xano
interface UsuarioApi {

    @POST("auth/signup")
    suspend fun registrarUsuario(@Body nuevoUsuario: NuevoUsuario): RespuestaToken

    @POST("auth/login")
    suspend fun iniciarSesion(@Body credenciales: Credenciales): RespuestaToken

    @GET("auth/me")
    suspend fun obtenerUsuarioActual(): Usuario

    @Multipart
    @POST("auth/foto")
    suspend fun subirFotoPerfil(@Part foto: MultipartBody.Part): Usuario
}