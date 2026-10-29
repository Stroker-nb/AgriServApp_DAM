package com.duoc.agriserv.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.agriserv.R

private val EsquemaColoresAgriServ = lightColorScheme(
    primary = VerdeBoton,
    onPrimary = Color.White,
    secondary = VerdeEncabezado,
    onSecondary = Color.White,
    tertiary = VerdeOscuro,
    background = VerdeFondo,
    onBackground = Color.Black,
    surface = BlancoVerdoso,
    onSurface = Color.Black,
    surfaceContainerHigh = BlancoVerdoso // fondo de los AlertDialog
)

// Las pantallas usan fondos fijos, por eso el tema es siempre claro y sin colores dinámicos
@Composable
fun AgriServTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaColoresAgriServ,
        typography = Typography,
        shapes = Shapes(extraSmall = RoundedCornerShape(12.dp)), // esquinas redondeadas en los campos de texto
        content = content
    )
}

// Banda superior con el logo y el nombre de la app, presente en todas las pantallas del mockup
@Composable
fun EncabezadoAgriServ() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeEncabezado)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_agriserv),
            contentDescription = "Logo de AgriServ",
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "AgriServ", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

// Campos de texto con fondo blanco, como en el mockup
@Composable
fun coloresCampoAgriServ() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    errorContainerColor = Color.White
)