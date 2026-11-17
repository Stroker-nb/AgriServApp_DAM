package com.duoc.agriserv.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente de Retrofit: una sola URL base, la del grupo de API de Xano
object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://PEGAR-AQUI-LA-URL-DE-SU-GRUPO.xano.io/api:XXXXXXXX/") // debe terminar en "/"
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}