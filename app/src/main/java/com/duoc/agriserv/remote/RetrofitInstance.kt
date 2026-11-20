package com.duoc.agriserv.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Dirección de la instancia de Xano; también se usa para armar la URL de las imágenes
    const val URL_INSTANCIA = "https://x8ki-letl-twmt.n7.xano.io"

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

    // Un solo Retrofit para todo el grupo de API; cada interfaz agrupa los endpoints de un tema
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("$URL_INSTANCIA/api:agriserv/") // debe terminar en "/"
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val usuarioApi: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }
}