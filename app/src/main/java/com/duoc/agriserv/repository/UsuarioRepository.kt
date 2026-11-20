package com.duoc.agriserv.repository

import android.content.SharedPreferences
import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.FotoPerfil
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.remote.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
class UsuarioRepository(private val preferencias: SharedPreferences) {

    private var usuarioConSesion: Usuario? = null

    // El token se lee del teléfono al crear el repositorio
    private var token: String? = preferencias.getString(CLAVE_TOKEN, null)

    // Crea la cuenta en Xano; si el correo ya existe, la API responde con un error HTTP
    suspend fun registrar(nuevoUsuario: NuevoUsuario) {
        RetrofitInstance.api.registrarUsuario(nuevoUsuario)
    }

    // Devuelve el usuario si las credenciales son correctas, o null si no lo son
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return try {
            val respuesta = RetrofitInstance.api.iniciarSesion(Credenciales(correo, contrasena))
            val usuario = RetrofitInstance.api.obtenerUsuarioActual("Bearer ${respuesta.authToken}")

            // Solo se guarda la sesión cuando todo salió bien
            token = respuesta.authToken
            preferencias.edit().putString(CLAVE_TOKEN, respuesta.authToken).apply()
            usuarioConSesion = usuario
            usuario
        } catch (e: HttpException) {
            // Un error 4xx significa credenciales rechazadas; otros errores se propagan
            if (e.code() in 400..499) null else throw e
        }
    }

    // Al abrir la app: si hay un token guardado, se le pregunta a Xano quién es el usuario
    suspend fun obtenerUsuarioConSesion(): Usuario? {
        usuarioConSesion?.let { return it }
        val tokenGuardado = token ?: return null

        return try {
            val usuario = RetrofitInstance.api.obtenerUsuarioActual("Bearer $tokenGuardado")
            usuarioConSesion = usuario
            usuario
        } catch (e: HttpException) {
            // Un error 4xx significa que el token ya no sirve (por ejemplo, venció): se borra
            if (e.code() in 400..499) cerrarSesion()
            null
        } catch (e: Exception) {
            // Sin conexión u otro fallo: se conserva el token para intentarlo la próxima vez
            null
        }
    }

    suspend fun cerrarSesion() {
        usuarioConSesion = null
        token = null
        preferencias.edit().remove(CLAVE_TOKEN).apply()
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

    private companion object {
        const val CLAVE_TOKEN = "auth_token"
    }
}

//QUE HACE ESTA CLASE?
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario