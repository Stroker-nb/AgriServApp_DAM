package com.duoc.agriserv.remote

import com.duoc.agriserv.model.Imagen
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Dirección de la instancia de Xano; también se usa para armar la URL de las imágenes
    const val URL_INSTANCIA = "https://x8ki-letl-twmt.n7.xano.io"

    // Dirección del grupo de API de AgriServ; debe terminar en "/"
    const val BASE_URL = "$URL_INSTANCIA/api:agriserv/"

    // Se asigna una vez al iniciar la app (MainActivity); entrega el token de la sesión abierta
    var obtenerToken: () -> String? = { null }

    // Agrega a cada petición el token de la sesión, si hay una abierta
    private val agregarToken = Interceptor { cadena ->
        val token = obtenerToken()
        val peticion = if (token != null) {
            cadena.request().newBuilder().header("Authorization", "Bearer $token").build()
        } else {
            cadena.request()
        }
        cadena.proceed(peticion)
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor(agregarToken)
        .build()

    // Arma un Retrofit contra la dirección indicada (en las pruebas se usa la de MockWebServer)
    private fun crearRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Un solo Retrofit para todo el grupo de API; cada interfaz agrupa los endpoints de un tema
    private val retrofit: Retrofit by lazy { crearRetrofit(BASE_URL) }

    // Equivalente a RetrofitClient.crearApi(baseUrl) del repo de clase: permite cambiar la dirección
    fun crearUsuarioApi(baseUrl: String = BASE_URL): UsuarioApi =
        crearRetrofit(baseUrl).create(UsuarioApi::class.java)

    val usuarioApi: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }

    val solicitudApi: SolicitudApi by lazy { retrofit.create(SolicitudApi::class.java) }

    // Arma la dirección completa de una imagen: Xano entrega la ruta sin el dominio de la instancia
    fun direccionImagen(imagen: Imagen?): String? =
        imagen?.url ?: imagen?.path?.let { URL_INSTANCIA + it }
}