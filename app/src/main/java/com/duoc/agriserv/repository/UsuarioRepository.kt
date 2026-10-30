package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario) = dao.insertar(usuario)

    suspend fun buscarPorCorreo(correo: String): Usuario? = dao.buscarPorCorreo(correo)

    suspend fun obtenerUsuarioConSesion(): Usuario? = dao.buscarUsuarioConSesion()

    // Solo puede haber un usuario con sesión activa a la vez
    suspend fun iniciarSesion(idUsuario: Int) {
        dao.cerrarTodasLasSesiones()
        dao.activarSesion(idUsuario)
    }

    suspend fun cerrarSesion() = dao.cerrarTodasLasSesiones()

}

//QUE HACE ESTA CLASE?
//Esta clase es el repositorio de usuario, que se encarga en si de operar con la bd,
//El intermediario entre la BD y los viewModel que ocupen cosas del usuario