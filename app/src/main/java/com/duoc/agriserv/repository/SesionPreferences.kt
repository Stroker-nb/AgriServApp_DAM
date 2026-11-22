package com.duoc.agriserv.repository

import android.content.SharedPreferences

/**
 * Guarda el token de la sesión abierta, para recordarla aunque se cierre la app.
 * Es una interfaz para que las pruebas puedan usar una versión falsa sin SharedPreferences.
 */
interface SesionPreferences {
    fun obtenerToken(): String?
    fun guardarToken(token: String)
    fun borrarToken()
}

/**
 * Versión real: guarda el token en el teléfono con SharedPreferences.
 * Es la única clase que lee y escribe ese dato; los repositorios no manejan el token.
 */
class SesionPreferencesImpl(private val preferencias: SharedPreferences) : SesionPreferences {

    override fun obtenerToken(): String? = preferencias.getString(CLAVE_TOKEN, null)

    override fun guardarToken(token: String) {
        preferencias.edit().putString(CLAVE_TOKEN, token).apply()
    }

    override fun borrarToken() {
        preferencias.edit().remove(CLAVE_TOKEN).apply()
    }

    private companion object {
        const val CLAVE_TOKEN = "auth_token"
    }
}