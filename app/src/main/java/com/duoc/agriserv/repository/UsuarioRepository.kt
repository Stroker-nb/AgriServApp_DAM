package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.duoc.agriserv.model.Solicitante
import com.duoc.agriserv.model.Ofertante

object UsuarioRepository {

    private var _usuarios by mutableStateOf(listOf<Usuario>())

    val usuarios: List<Usuario>
        get() = _usuarios

    private var nextId = 1

    // Crear un productor
    fun crearSolicitante(
        nombre: String,
        apellido: String,
        rut: String,
        correo: String
    ): Solicitante {

        val usuario = Solicitante(
            idUsuario = nextId++,
            nombreUsuario = nombre,
            apellidoUsuario = apellido,
            rutUsuario = rut,
            correoUsuario = correo,
            solicitudes = mutableListOf()
        )

        _usuarios = _usuarios + usuario
        return usuario
    }

    // Crear un aplicador
    fun crearOfertante(
        nombre: String,
        apellido: String,
        rut: String,
        correo: String
    ): Ofertante {

        val usuario = Ofertante(
            idUsuario = nextId++,
            nombreUsuario = nombre,
            apellidoUsuario = apellido,
            rutUsuario = rut,
            correoUsuario = correo,
            ofertas = mutableListOf()
        )

        _usuarios = _usuarios + usuario
        return usuario
    }

    // Obtener todos los usuarios
    fun obtenerUsuarios(): List<Usuario> = usuarios

    // Buscar usuario por ID
    fun buscarPorId(id: Int): Usuario? {
        return usuarios.find { it.idUsuario == id }
    }

    // Buscar usuario por correo
    fun buscarPorCorreo(correo: String): Usuario? {
        return usuarios.find {
            it.correoUsuario.equals(correo.trim(), ignoreCase = true)
        }
    }
}

