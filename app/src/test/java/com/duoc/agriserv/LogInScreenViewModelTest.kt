package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.repository.UsuarioRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogInScreenViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al abrir sin sesion guardada termina de cargar sin usuario`() = runTest {
        println("INICIO: comprobar la revision de sesion al abrir la app.")
        println("PREPARACION: el repositorio falso no tiene ninguna sesion guardada.")
        val viewModel = LogInScreenViewModel(FakeRepository())

        println("ACCION: iniciar la revision y ejecutar las corrutinas pendientes.")
        assertTrue(viewModel.estado.value.cargandoSesion)
        advanceUntilIdle()
        println("COMPROBACION: termino de cargar y no hay sesion.")
        assertFalse(viewModel.estado.value.cargandoSesion)
        assertNull(viewModel.estado.value.sesion)
        println("CORRECTO: no se encontro sesion y se puede mostrar el login.")
    }

    @Test
    fun `al abrir con sesion guardada publica el usuario`() = runTest {
        println("INICIO: comprobar que se recuerda la sesion guardada.")
        println("PREPARACION: el repositorio falso devolvera a Ana; no se usa internet.")
        val usuario = Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE)
        val viewModel = LogInScreenViewModel(FakeRepository(sesionGuardada = { usuario }))

        println("ACCION: ejecutar la revision de sesion.")
        advanceUntilIdle()
        println("COMPROBACION: el estado debe contener a Ana como sesion.")
        assertEquals(usuario, viewModel.estado.value.sesion)
        assertFalse(viewModel.estado.value.cargandoSesion)
        println("CORRECTO: se publico la sesion guardada.")
    }

    @Test
    fun `un correo invalido muestra error`() = runTest {
        println("INICIO: comprobar la validacion del correo.")
        println("PREPARACION: viewModel con repositorio falso.")
        val viewModel = LogInScreenViewModel(FakeRepository())
        advanceUntilIdle()

        println("ACCION: escribir un correo sin arroba.")
        viewModel.onCorreoChange("anacorreo.cl")
        println("COMPROBACION: debe aparecer el error de correo invalido.")
        assertEquals("Correo electrónico inválido", viewModel.estado.value.errorCorreo)
        println("ACCION: escribir un correo valido.")
        viewModel.onCorreoChange("ana@correo.cl")
        println("COMPROBACION: el error debe desaparecer.")
        assertNull(viewModel.estado.value.errorCorreo)
        println("CORRECTO: el correo se valida al escribir.")
    }

    @Test
    fun `iniciar sesion con campos vacios no llama al repositorio`() = runTest {
        println("INICIO: comprobar que no se consulta al servidor con el formulario vacio.")
        println("PREPARACION: el repositorio falso cuenta las llamadas.")
        var intentos = 0
        val viewModel = LogInScreenViewModel(FakeRepository(iniciarSesion = { _, _ ->
            intentos++
            null
        }))
        advanceUntilIdle()

        println("ACCION: pulsar iniciar sesion sin escribir nada.")
        viewModel.iniciarSesion()
        advanceUntilIdle()
        println("COMPROBACION: hay errores en ambos campos y no hubo llamadas.")
        assertEquals("Debes ingresar tu correo electrónico", viewModel.estado.value.errorCorreo)
        assertEquals("Debes ingresar una contraseña", viewModel.estado.value.errorContrasena)
        assertEquals(0, intentos)
        println("CORRECTO: el formulario vacio no llego al servidor.")
    }

    @Test
    fun `iniciar sesion correcto publica el usuario`() = runTest {
        println("INICIO: comprobar un inicio de sesion exitoso.")
        println("PREPARACION: el repositorio falso aceptara las credenciales de Ana.")
        val usuario = Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE)
        var correoRecibido: String? = null
        val viewModel = LogInScreenViewModel(FakeRepository(iniciarSesion = { correo, _ ->
            correoRecibido = correo
            usuario
        }))
        advanceUntilIdle()

        println("ACCION: escribir un correo con espacios y mayusculas, y pulsar iniciar sesion.")
        viewModel.onCorreoChange("  ANA@Correo.cl ")
        viewModel.onContrasenaChange("secreto1")
        viewModel.iniciarSesion()
        advanceUntilIdle()
        println("COMPROBACION: el repositorio recibio el correo limpio y el usuario quedo publicado.")
        assertEquals("ana@correo.cl", correoRecibido)
        assertEquals(usuario, viewModel.estado.value.usuarioLogueado)
        assertEquals(usuario, viewModel.estado.value.sesion)
        println("CORRECTO: se publico el usuario que inicio sesion.")
    }

    @Test
    fun `credenciales incorrectas publican un mensaje`() = runTest {
        println("INICIO: comprobar el rechazo de credenciales.")
        println("PREPARACION: el repositorio falso devolvera null, como cuando la API rechaza la clave.")
        val viewModel = LogInScreenViewModel(FakeRepository(iniciarSesion = { _, _ -> null }))
        advanceUntilIdle()

        println("ACCION: escribir datos validos y pulsar iniciar sesion.")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onContrasenaChange("clave-mala")
        viewModel.iniciarSesion()
        advanceUntilIdle()
        println("COMPROBACION: debe mostrarse el error y no debe haber usuario.")
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errorCredenciales)
        assertNull(viewModel.estado.value.usuarioLogueado)
        println("CORRECTO: las credenciales incorrectas no abrieron sesion.")
    }

    @Test
    fun `un error de red publica un mensaje`() = runTest {
        println("INICIO: comprobar el error de conexion al iniciar sesion.")
        println("PREPARACION: el repositorio falso lanzara IOException.")
        val viewModel = LogInScreenViewModel(FakeRepository(iniciarSesion = { _, _ ->
            throw IOException("Sin conexion")
        }))
        advanceUntilIdle()

        println("ACCION: escribir datos validos y pulsar iniciar sesion.")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onContrasenaChange("secreto1")
        viewModel.iniciarSesion()
        advanceUntilIdle()
        println("COMPROBACION: debe mostrarse el mensaje de conexion.")
        assertEquals(
            "No se pudo conectar con el servidor. Inténtalo de nuevo",
            viewModel.estado.value.errorCredenciales
        )
        println("CORRECTO: el error de conexion se informo.")
    }

    @Test
    fun `cerrar sesion limpia el estado`() = runTest {
        println("INICIO: comprobar el cierre de sesion.")
        println("PREPARACION: hay una sesion guardada y el repositorio falso cuenta los cierres.")
        val usuario = Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE)
        var cierres = 0
        val viewModel = LogInScreenViewModel(FakeRepository(
            sesionGuardada = { usuario },
            cerrar = { cierres++ }
        ))
        advanceUntilIdle()

        println("ACCION: cerrar sesion.")
        viewModel.cerrarSesion()
        advanceUntilIdle()
        println("COMPROBACION: el estado vuelve al inicial y el repositorio fue avisado una vez.")
        assertEquals(LoginUiState(cargandoSesion = false), viewModel.estado.value)
        assertEquals(1, cierres)
        println("CORRECTO: la sesion se cerro.")
    }

    private class FakeRepository(
        private val sesionGuardada: suspend () -> Usuario? = { null },
        private val iniciarSesion: suspend (String, String) -> Usuario? = { _, _ -> null },
        private val cerrar: suspend () -> Unit = {}
    ) : UsuarioRepository {
        override suspend fun registrar(nuevoUsuario: NuevoUsuario) = Unit
        override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? =
            iniciarSesion.invoke(correo, contrasena)
        override suspend fun obtenerUsuarioConSesion(): Usuario? = sesionGuardada()
        override suspend fun cerrarSesion() = cerrar()
        override suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario =
            throw UnsupportedOperationException()
        override fun direccionFoto(foto: Imagen?): String? = null
    }
}