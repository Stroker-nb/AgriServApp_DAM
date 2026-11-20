package com.duoc.agriserv.repository

import android.content.SharedPreferences

/**
 * Guarda en el teléfono el token de la sesión abierta, para recordarla aunque se cierre la app.
 * Es la única clase que lee y escribe ese dato; los repositorios no manejan el token.
 */
class SesionPreferences(private val preferencias: SharedPreferences) {

    fun obtenerToken(): String? = preferencias.getString(CLAVE_TOKEN, null)

    fun guardarToken(token: String) {
        preferencias.edit().putString(CLAVE_TOKEN, token).apply()
    }

    fun borrarToken() {
        preferencias.edit().remove(CLAVE_TOKEN).apply()
    }

    private companion object {
        const val CLAVE_TOKEN = "auth_token"
    }
}