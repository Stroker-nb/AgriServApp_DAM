package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.remote.RetrofitInstance
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class UsuarioRepositoryTest {
    private lateinit var servidor: MockWebServer
    private lateinit var sesion: FakeSesion
    private lateinit var repository: UsuarioRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        sesion = FakeSesion()
        // El cliente real agrega el token de la sesion a cada peticion
        RetrofitInstance.obtenerToken = { sesion.obtenerToken() }
        val api = RetrofitInstance.crearUsuarioApi(servidor.url("/api:agriserv/").toString())
        repository = UsuarioRepositoryImpl(api, sesion)
    }

    @After
    fun limpiar() {
        RetrofitInstance.obtenerToken = { null }
        servidor.shutdown()
    }

    @Test
    fun `registrar consulta el endpoint de signup`() = runTest {
        println("INICIO: comprobar la solicitud de registro.")
        println("PREPARACION: el servidor local simulado respondera con un token.")
        servidor.enqueue(MockResponse().setBody("""{"authToken":"abc"}"""))

        println("ACCION: el repositorio registra un usuario en el servidor local, no en Xano.")
        repository.registrar(
            NuevoUsuario("Ana", "Soto", "12.345.678-5", "ana@correo.cl", "secreto1", Rol.SOLICITANTE)
        )

        println("COMPROBACION: debe usar POST /auth/signup con los datos en el cuerpo.")
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:agriserv/auth/signup", solicitud.path)
        assertTrue(solicitud.body.readUtf8().contains(""""correo":"ana@correo.cl""""))
        println("CORRECTO: la solicitud de registro es la esperada.")
    }

    @Test
    fun `registrar propaga errores http`() = runTest {
        println("INICIO: comprobar la propagacion de errores HTTP al registrar.")
        println("PREPARACION: el servidor local simulado respondera con HTTP 403.")
        servidor.enqueue(MockResponse().setResponseCode(403))

        println("ACCION: registrar un usuario en el servidor local.")
        try {
            repository.registrar(
                NuevoUsuario("Ana", "Soto", "12.345.678-5", "ana@correo.cl", "secreto1", Rol.SOLICITANTE)
            )
            throw AssertionError("Se esperaba un HttpException")
        } catch (error: HttpException) {
            println("COMPROBACION: el error recibido debe tener codigo 403.")
            assertEquals(403, error.code())
        }
        println("CORRECTO: el repositorio propago el error HTTP 403.")
    }

    @Test
    fun `iniciar sesion guarda el token y convierte el usuario`() = runTest {
        println("INICIO: comprobar un inicio de sesion exitoso.")
        println("PREPARACION: el servidor simulado respondera con un token y luego con Ana.")
        servidor.enqueue(MockResponse().setBody("""{"authToken":"token123"}"""))
        servidor.enqueue(MockResponse().setBody(
            """{"id":1,"nombre":"Ana","apellido":"Soto","rut":"12.345.678-5","correo":"ana@correo.cl","rol":"SOLICITANTE","calificacion":0.0,"foto":null}"""
        ))

        println("ACCION: iniciar sesion contra el servidor local.")
        val resultado = repository.iniciarSesion("ana@correo.cl", "secreto1")

        println("COMPROBACION: el JSON debe convertirse en un usuario y el token debe quedar guardado.")
        assertEquals(
            Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE),
            resultado
        )
        assertEquals("token123", sesion.token)
        println("COMPROBACION: primero POST /auth/login y luego GET /auth/me con el token.")
        val login = servidor.takeRequest()
        assertEquals("POST", login.method)
        assertEquals("/api:agriserv/auth/login", login.path)
        val me = servidor.takeRequest()
        assertEquals("GET", me.method)
        assertEquals("/api:agriserv/auth/me", me.path)
        assertEquals("Bearer token123", me.getHeader("Authorization"))
        println("CORRECTO: el token se guardo y se envio en la peticion siguiente.")
    }

    @Test
    fun `credenciales rechazadas devuelven null y no dejan token`() = runTest {
        println("INICIO: comprobar el rechazo de credenciales.")
        println("PREPARACION: el servidor local simulado respondera con HTTP 403.")
        servidor.enqueue(MockResponse().setResponseCode(403))

        println("ACCION: iniciar sesion contra el servidor local.")
        val resultado = repository.iniciarSesion("ana@correo.cl", "clave-mala")

        println("COMPROBACION: debe devolver null y no debe quedar ningun token guardado.")
        assertNull(resultado)
        assertNull(sesion.token)
        println("CORRECTO: las credenciales rechazadas no dejaron sesion.")
    }

    @Test
    fun `sin token guardado no hay sesion y no se llama al servidor`() = runTest {
        println("INICIO: comprobar la revision de sesion sin token.")
        println("PREPARACION: la sesion falsa no tiene ningun token.")

        println("ACCION: pedir el usuario con sesion.")
        val resultado = repository.obtenerUsuarioConSesion()

        println("COMPROBACION: debe devolver null sin hacer ninguna peticion.")
        assertNull(resultado)
        assertEquals(0, servidor.requestCount)
        println("CORRECTO: sin token no se consulto al servidor.")
    }

    @Test
    fun `con token valido devuelve el usuario`() = runTest {
        println("INICIO: comprobar la sesion guardada con un token valido.")
        println("PREPARACION: hay un token guardado y el servidor simulado respondera con Ana.")
        sesion.token = "token123"
        servidor.enqueue(MockResponse().setBody(
            """{"id":1,"nombre":"Ana","apellido":"Soto","rut":"12.345.678-5","correo":"ana@correo.cl","rol":"SOLICITANTE","calificacion":0.0,"foto":null}"""
        ))

        println("ACCION: pedir el usuario con sesion.")
        val resultado = repository.obtenerUsuarioConSesion()

        println("COMPROBACION: debe devolver a Ana usando GET /auth/me con el token.")
        assertEquals(
            Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE),
            resultado
        )
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:agriserv/auth/me", solicitud.path)
        assertEquals("Bearer token123", solicitud.getHeader("Authorization"))
        println("CORRECTO: el usuario se obtuvo con su token.")
    }

    @Test
    fun `un token vencido se borra`() = runTest {
        println("INICIO: comprobar un token vencido.")
        println("PREPARACION: hay un token guardado y el servidor local respondera HTTP 401.")
        sesion.token = "token-vencido"
        servidor.enqueue(MockResponse().setResponseCode(401))

        println("ACCION: pedir el usuario con sesion.")
        val resultado = repository.obtenerUsuarioConSesion()

        println("COMPROBACION: debe devolver null y borrar el token.")
        assertNull(resultado)
        assertNull(sesion.token)
        println("CORRECTO: el token vencido se elimino.")
    }

    @Test
    fun `un error de servidor conserva el token`() = runTest {
        println("INICIO: comprobar un fallo temporal del servidor.")
        println("PREPARACION: hay un token guardado y el servidor local respondera HTTP 500.")
        sesion.token = "token123"
        servidor.enqueue(MockResponse().setResponseCode(500))

        println("ACCION: pedir el usuario con sesion.")
        val resultado = repository.obtenerUsuarioConSesion()

        println("COMPROBACION: debe devolver null pero conservar el token para reintentar.")
        assertNull(resultado)
        assertEquals("token123", sesion.token)
        println("CORRECTO: el token se conservo.")
    }

    @Test
    fun `subir la foto envia un multipart y convierte la respuesta`() = runTest {
        println("INICIO: comprobar la subida de la foto de perfil.")
        println("PREPARACION: el servidor simulado respondera con Ana y su nueva foto.")
        servidor.enqueue(MockResponse().setBody(
            """{"id":1,"nombre":"Ana","apellido":"Soto","rut":"12.345.678-5","correo":"ana@correo.cl","rol":"SOLICITANTE","calificacion":0.0,"foto":{"path":"/vault/ana.png","url":null}}"""
        ))

        println("ACCION: subir una imagen PNG al servidor local.")
        val resultado = repository.subirFotoPerfil("abc".toByteArray(), "image/png")

        println("COMPROBACION: debe usar POST /auth/foto como multipart con archivo perfil.png.")
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:agriserv/auth/foto", solicitud.path)
        assertTrue(solicitud.getHeader("Content-Type")!!.startsWith("multipart/form-data"))
        assertTrue(solicitud.body.readUtf8().contains("""filename="perfil.png""""))
        assertEquals(Imagen(path = "/vault/ana.png"), resultado.foto)
        println("CORRECTO: la foto se subio y el usuario vino actualizado.")
    }

    // Sesion falsa: guarda el token en memoria en vez de usar SharedPreferences
    private class FakeSesion(var token: String? = null) : SesionPreferences {
        override fun obtenerToken(): String? = token
        override fun guardarToken(token: String) {
            this.token = token
        }
        override fun borrarToken() {
            token = null
        }
    }
}