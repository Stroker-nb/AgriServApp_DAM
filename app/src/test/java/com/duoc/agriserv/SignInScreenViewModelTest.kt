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
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SignInScreenViewModelTest {
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
    fun `un rut con digito verificador incorrecto muestra error`() = runTest {
        println("INICIO: comprobar la validacion del RUT.")
        println("PREPARACION: viewModel con repositorio falso.")
        val viewModel = SignInScreenViewModel(FakeRepository())

        println("ACCION: escribir un RUT con digito verificador incorrecto.")
        viewModel.onRutChange("12.345.678-9")
        println("COMPROBACION: debe aparecer el error de RUT invalido.")
        assertEquals("RUT inválido (ej: 12.345.678-5)", viewModel.estado.value.errorRut)
        println("ACCION: escribir el RUT correcto.")
        viewModel.onRutChange("12.345.678-5")
        println("COMPROBACION: el error debe desaparecer.")
        assertNull(viewModel.estado.value.errorRut)
        println("CORRECTO: el RUT se valida con su digito verificador.")
    }

    @Test
    fun `registrar con el formulario vacio no llama al repositorio`() = runTest {
        println("INICIO: comprobar que no se registra con el formulario vacio.")
        println("PREPARACION: el repositorio falso cuenta las llamadas.")
        var intentos = 0
        val viewModel = SignInScreenViewModel(FakeRepository(registrar = { intentos++ }))

        println("ACCION: pulsar registrar sin escribir nada.")
        viewModel.registrar()
        advanceUntilIdle()
        println("COMPROBACION: todos los campos tienen error y no hubo llamadas.")
        val estado = viewModel.estado.value
        assertEquals("Debes ingresar tu nombre", estado.errorNombre)
        assertEquals("Debes ingresar tu apellido", estado.errorApellido)
        assertEquals("Debes ingresar tu RUT", estado.errorRut)
        assertEquals("Debes ingresar tu correo electrónico", estado.errorCorreo)
        assertEquals("Debes ingresar una contraseña", estado.errorContrasena)
        assertEquals(0, intentos)
        println("CORRECTO: el formulario vacio no llego al servidor.")
    }

    @Test
    fun `registrar con datos validos envia el usuario y marca exito`() = runTest {
        println("INICIO: comprobar un registro exitoso.")
        println("PREPARACION: el repositorio falso guardara el usuario que recibe.")
        var recibido: NuevoUsuario? = null
        val viewModel = SignInScreenViewModel(FakeRepository(registrar = { recibido = it }))

        println("ACCION: llenar el formulario con espacios y mayusculas, elegir rol y registrar.")
        viewModel.onNombreChange("  Ana ")
        viewModel.onApellidoChange("Soto")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onCorreoChange("  ANA@Correo.cl ")
        viewModel.onContrasenaChange("secreto1")
        viewModel.onRolChange(Rol.OFERTANTE)
        viewModel.registrar()
        advanceUntilIdle()
        println("COMPROBACION: el repositorio recibio los datos limpios y el registro fue exitoso.")
        assertEquals(
            NuevoUsuario("Ana", "Soto", "12.345.678-5", "ana@correo.cl", "secreto1", Rol.OFERTANTE),
            recibido
        )
        assertTrue(viewModel.estado.value.registroExitoso)
        println("CORRECTO: el usuario se registro con los datos esperados.")
    }

    @Test
    fun `un correo repetido muestra error en el correo`() = runTest {
        println("INICIO: comprobar el registro con un correo que ya existe.")
        println("PREPARACION: el repositorio falso respondera con un error HTTP 403, como Xano.")
        val viewModel = SignInScreenViewModel(FakeRepository(registrar = {
            throw HttpException(Response.error<Any>(403, "".toResponseBody()))
        }))

        println("ACCION: llenar el formulario con datos validos y registrar.")
        viewModel.onNombreChange("Ana")
        viewModel.onApellidoChange("Soto")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onContrasenaChange("secreto1")
        viewModel.registrar()
        advanceUntilIdle()
        println("COMPROBACION: el error debe quedar en el campo correo y no hay exito.")
        assertEquals("Este correo ya está registrado", viewModel.estado.value.errorCorreo)
        assertFalse(viewModel.estado.value.registroExitoso)
        println("CORRECTO: el correo repetido se informo en su campo.")
    }

    @Test
    fun `un error http distinto publica error de servidor`() = runTest {
        println("INICIO: comprobar un error HTTP 500.")
        println("PREPARACION: el repositorio falso respondera con un error HTTP 500.")
        val viewModel = SignInScreenViewModel(FakeRepository(registrar = {
            throw HttpException(Response.error<Any>(500, "".toResponseBody()))
        }))

        println("ACCION: llenar el formulario con datos validos y registrar.")
        viewModel.onNombreChange("Ana")
        viewModel.onApellidoChange("Soto")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onContrasenaChange("secreto1")
        viewModel.registrar()
        advanceUntilIdle()
        println("COMPROBACION: debe mostrarse el error general de servidor.")
        assertEquals(
            "No se pudo crear la cuenta. Inténtalo de nuevo",
            viewModel.estado.value.errorServidor
        )
        assertFalse(viewModel.estado.value.registroExitoso)
        println("CORRECTO: el error 500 se informo como error de servidor.")
    }

    @Test
    fun `un error de red publica un mensaje`() = runTest {
        println("INICIO: comprobar el registro sin conexion.")
        println("PREPARACION: el repositorio falso lanzara IOException.")
        val viewModel = SignInScreenViewModel(FakeRepository(registrar = {
            throw IOException("Sin conexion")
        }))

        println("ACCION: llenar el formulario con datos validos y registrar.")
        viewModel.onNombreChange("Ana")
        viewModel.onApellidoChange("Soto")
        viewModel.onRutChange("12.345.678-5")
        viewModel.onCorreoChange("ana@correo.cl")
        viewModel.onContrasenaChange("secreto1")
        viewModel.registrar()
        advanceUntilIdle()
        println("COMPROBACION: debe mostrarse el mensaje de conexion.")
        assertEquals(
            "No se pudo conectar con el servidor. Inténtalo de nuevo",
            viewModel.estado.value.errorServidor
        )
        assertFalse(viewModel.estado.value.registroExitoso)
        println("CORRECTO: el error de conexion se informo.")
    }

    private class FakeRepository(
        private val registrar: suspend (NuevoUsuario) -> Unit = {}
    ) : UsuarioRepository {
        override suspend fun registrar(nuevoUsuario: NuevoUsuario) = registrar.invoke(nuevoUsuario)
        override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? = null
        override suspend fun obtenerUsuarioConSesion(): Usuario? = null
        override suspend fun cerrarSesion() = Unit
        override suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario =
            throw UnsupportedOperationException()
        override fun direccionFoto(foto: Imagen?): String? = null
    }
}