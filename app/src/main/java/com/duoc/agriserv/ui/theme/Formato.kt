package com.duoc.agriserv.viewModel

import java.text.NumberFormat
import java.util.Locale

// Convierte un monto entero en pesos chilenos con separador de miles: 120000 -> "$120.000"
fun Int.comoPesos(): String =
    "$" + NumberFormat.getIntegerInstance(Locale("es", "CL")).format(this)