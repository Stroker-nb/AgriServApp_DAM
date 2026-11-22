package com.duoc.agriserv.viewModel

import com.duoc.agriserv.model.Aplicacion
import com.duoc.agriserv.model.TipoProducto

/**
 * Estado de la pantalla de inicio del aplicador: si está cargando, si hubo un error, la lista de
 * solicitudes ya filtrada y con los textos listos, los filtros elegidos y si se conoce su ubicación.
 */
data class OfertanteUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val solicitudes: List<SolicitudItem> = emptyList(),
    val filtroTipoProducto: TipoProducto? = null,
    val filtroAplicacion: Aplicacion? = null,
    val filtroSuperficieMinima: String = "",
    val ubicacionConocida: Boolean = false,
    val avisoUbicacion: String? = null
) {
    val hayFiltros: Boolean
        get() = filtroTipoProducto != null || filtroAplicacion != null || filtroSuperficieMinima.isNotBlank()
}