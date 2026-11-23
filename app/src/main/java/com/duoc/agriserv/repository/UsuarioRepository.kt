package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Credenciales
import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.remote.RetrofitInstance
import com.duoc.agriserv.remote.UsuarioApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

// Contrato del repositorio de usuarios: los ViewModel dependen de esta interfaz,
// así las pruebas pueden entregarles un repositorio falso (igual que PersonajesRepository en el repo de clase)
interface UsuarioRepository {
    suspend fun registrar(nuevoUsuario: NuevoUsuario)
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario?
    suspend fun obtenerUsuarioConSesion(): Usuario?
    suspend fun cerrarSesion()
    suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario
    fun direccionFoto(foto: Imagen?): String?
}

/**
 * Intermediario entre los ViewModel y la API de Xano para todo lo relacionado con usuarios.
 * Xano valida las contraseñas y evita los correos repetidos. El token de la sesión lo guarda
 * SesionPreferences y se envía solo en cada petición, así la sesión se recuerda al cerrar la app.
 */
class UsuarioRepositoryImpl(
    private val api: UsuarioApi,
    private val sesion: SesionPreferences
) : UsuarioRepository {

    private var usuarioConSesion: Usuario? = null

    // Crea la cuenta en Xano; si el correo ya existe, la API responde con un error HTTP
    override suspend fun registrar(nuevoUsuario: NuevoUsuario) {
        api.registrarUsuario(nuevoUsuario)
    }

    // Devuelve el usuario si las credenciales son correctas, o null si no lo son
    override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return try {
            val respuesta = api.iniciarSesion(Credenciales(correo, contrasena))

            // Se guarda primero: el interceptor lo envía en la petición siguiente
            sesion.guardarToken(respuesta.authToken)
            val usuario = api.obtenerUsuarioActual()

            usuarioConSesion = usuario
            usuario
        } catch (e: Exception) {
            // Si algo falló, no se deja guardado un token a medias
            sesion.borrarToken()
            // Un error 4xx significa credenciales rechazadas; los demás errores se propagan
            if (e is HttpException && e.code() in 400..499) null else throw e
        }
    }

    // Al abrir la app: si hay un token guardado, se le pregunta a Xano quién es el usuario
    override suspend fun obtenerUsuarioConSesion(): Usuario? {
        usuarioConSesion?.let { return it }
        if (sesion.obtenerToken() == null) return null

        return try {
            val usuario = api.obtenerUsuarioActual()
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

    override suspend fun cerrarSesion() {
        usuarioConSesion = null
        sesion.borrarToken()
    }

    // Sube la foto de perfil a Xano y devuelve el usuario actualizado
    override suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario {
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
        val usuario = api.subirFotoPerfil(parte)
        usuarioConSesion = usuario
        return usuario
    }

    // Arma la dirección completa de la foto: Xano entrega la ruta sin el dominio de la instancia
    override fun direccionFoto(foto: Imagen?): String? = RetrofitInstance.direccionImagen(foto)
}

//QUE HACE ESTA CLASE?
//UsuarioRepository es el contrato (interfaz) y UsuarioRepositoryImpl la clase real que lo cumple.
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario