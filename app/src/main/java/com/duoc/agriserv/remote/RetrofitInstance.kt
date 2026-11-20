package com.duoc.agriserv.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Dirección de la instancia de Xano; también se usa para armar la URL de las imágenes
    const val URL_INSTANCIA = "https://x8ki-letl-twmt.n7.xano.io"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("$URL_INSTANCIA/api:agriserv/") // debe terminar en "/"
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}