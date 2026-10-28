package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun registrar(usuario: Usuario) = dao.insertar(usuario)

    suspend fun buscarPorCorreo(correo: String): Usuario? = dao.buscarPorCorreo(correo)
}

