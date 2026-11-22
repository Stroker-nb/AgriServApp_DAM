package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevoUsuario
import com.duoc.agriserv.model.Rol
import com.duoc.agriserv.model.Usuario
import com.duoc.agriserv.repository.UsuarioRepository
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileScreenViewModelTest {
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
    fun `cargar el perfil de un solicitante lo publica como productor`() = runTest {
        println("INICIO: comprobar el perfil de un solicitante.")
        println("PREPARACION: el repositorio falso devolvera a Ana con rol SOLICITANTE.")
        val usuario = Usuario(1, "Ana", "Soto", "12.345.678-5", "ana@correo.cl", Rol.SOLICITANTE)
        val viewModel = ProfileScreenViewModel(FakeRepository { usuario })

        println("ACCION: cargar el perfil y ejecutar las corrutinas pendientes.")
        viewModel.cargarPerfil()
        advanceUntilIdle()
        println("COMPROBACION: los datos deben estar listos para la pantalla.")
        val estado = viewModel.estado.value
        assertTrue(estado.cargado)
        assertEquals("Ana Soto", estado.nombreCompleto)
        assertEquals("Productor", estado.tipoCuenta)
        assertEquals("ana@correo.cl", estado.correo)
        assertEquals("12.345.678-5", estado.rut)
        assertFalse(estado.esOfertante)
        println("CORRECTO: se publico el perfil del productor.")
    }

    @Test
    fun `cargar el perfil de un ofertante lo publica como aplicador`() = runTest {
        println("INICIO: comprobar el perfil de un ofertante.")
        println("PREPARACION: el repositorio falso devolvera un OFERTANTE con calificacion 4.5.")
        val usuario = Usuario(2, "Luis", "Rojas", "10.000.013-K", "luis@correo.cl", Rol.OFERTANTE, 4.5)
        val viewModel = ProfileScreenViewModel(FakeRepository { usuario })

        println("ACCION: cargar el perfil y ejecutar las corrutinas pendientes.")
        viewModel.cargarPerfil()
        advanceUntilIdle()
        println("COMPROBACION: debe verse como aplicador y tener calificaciones.")
        val estado = viewModel.estado.value
        assertEquals("Aplicador", estado.tipoCuenta)
        assertTrue(estado.esOfertante)
        assertEquals(4.5, estado.calificacion, 0.0)
        assertTrue(estado.tieneCalificaciones)
        println("CORRECTO: se publico el perfil del aplicador.")
    }

    @Test
    fun `sin sesion el perfil no se carga`() = runTest {
        println("INICIO: comprobar el perfil cuando no hay sesion.")
        println("PREPARACION: el repositorio falso no devolvera ningun usuario.")
        val viewModel = ProfileScreenViewModel(FakeRepository { null })

        println("ACCION: cargar el perfil y ejecutar las corrutinas pendientes.")
        viewModel.cargarPerfil()
        advanceUntilIdle()
        println("COMPROBACION: el estado debe seguir siendo el inicial.")
        assertEquals(ProfileUiState(), viewModel.estado.value)
        println("CORRECTO: no se mostraron datos sin sesion.")
    }

    private class FakeRepository(
        private val sesionGuardada: suspend () -> Usuario?
    ) : UsuarioRepository {
        override suspend fun registrar(nuevoUsuario: NuevoUsuario) = Unit
        override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? = null
        override suspend fun obtenerUsuarioConSesion(): Usuario? = sesionGuardada()
        override suspend fun cerrarSesion() = Unit
        override suspend fun subirFotoPerfil(imagen: ByteArray, tipoMime: String): Usuario =
            throw UnsupportedOperationException()
        override fun direccionFoto(foto: Imagen?): String? = null
    }
}