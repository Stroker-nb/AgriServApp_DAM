package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario

class UsuarioRepository {

    private val usuarios = mutableListOf<Usuario>()
    private var siguienteId = 1
    private var idUsuarioConSesion: Int? = null

    suspend fun registrar(usuario: Usuario) {
        usuarios.add(usuario.copy(idUsuario = siguienteId++))
    }

    // Devuelve null si no existe ninguna cuenta con ese correo
    suspend fun buscarPorCorreo(correo: String): Usuario? = usuarios.find { it.correo == correo }

    // Al abrir la app sirve para saber si hay una sesión abierta
    suspend fun obtenerUsuarioConSesion(): Usuario? =
        usuarios.find { it.idUsuario == idUsuarioConSesion }

    suspend fun iniciarSesion(idUsuario: Int) {
        idUsuarioConSesion = idUsuario
    }

    suspend fun cerrarSesion() {
        idUsuarioConSesion = null
    }
}

//QUE HACE ESTA CLASE?
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario