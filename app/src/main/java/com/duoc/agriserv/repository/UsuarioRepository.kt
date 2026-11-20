package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.remote.RetrofitInstance
import retrofit2.HttpException
import com.duoc.agriserv.model.FotoPerfil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
class UsuarioRepository {

    private var usuarioConSesion: Usuario? = null
    private var token: String? = null

    // Crea la cuenta en Xano; si el correo ya existe, la API responde con un error HTTP
    suspend fun registrar(nuevoUsuario: NuevoUsuario) {
        RetrofitInstance.api.registrarUsuario(nuevoUsuario)
    }

    // Devuelve el usuario si las credenciales son correctas, o null si no lo son
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return try {
            val respuesta = RetrofitInstance.api.iniciarSesion(Credenciales(correo, contrasena))
            token = respuesta.authToken

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
        token = null
    }

    // Sube la foto de perfil a Xano y devuelve el usuario actualizado
    suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario {
        val tokenActual = token ?: throw IllegalStateException("No hay una sesión abierta")
        val extension = when (tipoMime) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }
        val parte = MultipartBody.Part.createFormData(
            "foto",
            "perfil.$extension",
            imagen.toRequestBody(tipoMime.toMediaType())
        )
        val usuario = RetrofitInstance.api.subirFotoPerfil("Bearer $tokenActual", parte)
        usuarioConSesion = usuario
        return usuario
    }

    // Arma la dirección completa de la foto: Xano entrega la ruta sin el dominio de la instancia
    fun direccionFoto(foto: FotoPerfil?): String? =
        foto?.url ?: foto?.path?.let { RetrofitInstance.URL_INSTANCIA + it }
}

//QUE HACE ESTA CLASE?
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario