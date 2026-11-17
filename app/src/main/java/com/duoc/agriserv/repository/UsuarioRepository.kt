package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.remote.RetrofitInstance
import retrofit2.HttpException

/**
 * Intermediario entre los ViewModel y la API de Xano para todo lo relacionado con usuarios.
 * Xano valida las contraseñas y evita los correos repetidos; la sesión abierta vive solo en
 * memoria, así que se pierde al cerrar la app.
 */
class UsuarioRepository {

    private var usuarioConSesion: Usuario? = null

    // Crea la cuenta en Xano; si el correo ya existe, la API responde con un error HTTP
    suspend fun registrar(nuevoUsuario: NuevoUsuario) {
        RetrofitInstance.api.registrarUsuario(nuevoUsuario)
    }

    // Devuelve el usuario si las credenciales son correctas, o null si no lo son
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return try {
            val respuesta = RetrofitInstance.api.iniciarSesion(Credenciales(correo, contrasena))
            val usuario = RetrofitInstance.api.obtenerUsuarioActual("Bearer ${respuesta.authToken}")
            usuarioConSesion = usuario
            usuario
        } catch (e: HttpException) {
            // Un error 4xx significa credenciales rechazadas; otros errores se propagan
            if (e.code() in 400..499) null else throw e
        }
    }

    // Al abrir la app sirve para saber si hay una sesión abierta
    suspend fun obtenerUsuarioConSesion(): Usuario? = usuarioConSesion

    suspend fun cerrarSesion() {
        usuarioConSesion = null
    }
}

//QUE HACE ESTA CLASE?
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario