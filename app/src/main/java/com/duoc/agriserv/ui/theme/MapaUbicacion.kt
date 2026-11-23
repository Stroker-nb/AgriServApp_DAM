package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Centro del mapa cuando todavía no hay una ubicación marcada (Santiago)
private val SANTIAGO = LatLng(-33.45, -70.66)

/**
 * Mapa de Google con un marcador en la ubicación indicada. Con alElegirUbicacion el usuario puede
 * mover el mapa y tocarlo para marcar un punto; sin ella es de solo lectura, y un toque sobre el
 * mapa ejecuta alPulsar (así no choca con el desplazamiento de la pantalla que lo contiene).
 */
@Composable
fun MapaUbicacion(
    latitud: Double?,
    longitud: Double?,
    modifier: Modifier = Modifier,
    alElegirUbicacion: ((Double, Double) -> Unit)? = null,
    alPulsar: (() -> Unit)? = null
) {
    val interactivo = alElegirUbicacion != null
    val ubicacion = if (latitud != null && longitud != null) LatLng(latitud, longitud) else null

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubicacion ?: SANTIAGO, if (ubicacion != null) 12f else 9f)
    }

    // Cuando cambia la ubicación, la cámara se desplaza hasta ella
    LaunchedEffect(latitud, longitud) {
        ubicacion?.let { camara.animate(CameraUpdateFactory.newLatLngZoom(it, 12f)) }
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = camara,
            uiSettings = MapUiSettings(
                scrollGesturesEnabled = interactivo,
                zoomGesturesEnabled = interactivo,
                zoomControlsEnabled = interactivo,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapClick = { punto -> alElegirUbicacion?.invoke(punto.latitude, punto.longitude) }
        ) {
            if (ubicacion != null) {
                Marker(
                    state = MarkerState(position = ubicacion),
                    title = "Ubicación aproximada"
                )
            }
        }

        // En modo solo lectura, una capa transparente recibe los toques para que el mapa no
        // bloquee el desplazamiento de la pantalla
        if (!interactivo) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { alPulsar?.invoke() }
            )
        }
    }
}