package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.UsuarioPublico

/**
 * Estado de la pantalla de perfil público: si está cargando, si hubo un error y los datos que
 * cualquier usuario puede ver de otro (sin RUT ni correo).
 */
data class PerfilPublicoUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val perfil: UsuarioPublico? = null,
    val fotoUrl: String? = null
)