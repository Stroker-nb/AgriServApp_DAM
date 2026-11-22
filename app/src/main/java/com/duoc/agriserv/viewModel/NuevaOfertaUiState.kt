package com.duoc.agriserv.viewModel

/**
 * Estado de la pantalla "Enviar oferta": lo que escribió el usuario, el error de cada campo y el
 * resultado del envío.
 */
data class NuevaOfertaUiState(
    val precio: String = "",
    val comentario: String = "",
    val errorPrecio: String? = null,
    val errorComentario: String? = null,
    val enviando: Boolean = false,
    val errorServidor: String? = null,
    val ofertaCreada: Boolean = false
)