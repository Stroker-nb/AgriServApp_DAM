package com.duoc.agriserv.repository

import com.duoc.agriserv.model.Imagen
import com.duoc.agriserv.model.NuevaOferta
import com.duoc.agriserv.model.Oferta
import com.duoc.agriserv.remote.OfertaApi
import com.duoc.agriserv.remote.RetrofitInstance

// Contrato del repositorio de ofertas: los ViewModel dependen de esta interfaz
interface OfertaRepository {
    suspend fun crearOferta(oferta: NuevaOferta): Oferta
    suspend fun obtenerMisOfertas(): List<Oferta>
    suspend fun obtenerOfertasDeSolicitud(idSolicitud: Int): List<Oferta>
    suspend fun aceptarOferta(idOferta: Int)
    fun direccionImagen(imagen: Imagen?): String?
}

/**
 * Intermediario entre los ViewModel y la API de Xano para todo lo relacionado con ofertas.
 * El token de la sesión se envía solo en cada petición.
 */
class OfertaRepositoryImpl(private val api: OfertaApi) : OfertaRepository {

    // Solo un aplicador puede ofertar, y una sola vez por solicitud; si no, la API responde con error HTTP
    override suspend fun crearOferta(oferta: NuevaOferta): Oferta = api.crearOferta(oferta)

    // Ofertas del aplicador con la sesión abierta, de la más nueva a la más antigua
    override suspend fun obtenerMisOfertas(): List<Oferta> = api.obtenerMisOfertas()

    // Ofertas de una solicitud, de la más barata a la más cara; solo las ve el dueño de la solicitud
    override suspend fun obtenerOfertasDeSolicitud(idSolicitud: Int): List<Oferta> =
        api.obtenerOfertasDeSolicitud(idSolicitud)

    // Solo el dueño de la solicitud puede aceptar una oferta, y solo si la solicitud sigue publicada
    override suspend fun aceptarOferta(idOferta: Int) {
        api.aceptarOferta(idOferta)
    }

    override fun direccionImagen(imagen: Imagen?): String? = RetrofitInstance.direccionImagen(imagen)
}