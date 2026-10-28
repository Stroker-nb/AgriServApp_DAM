

package com.duoc.agriserv.viewModel

import android.util.Patterns

// Reglas de validación de los formularios.
// Cada función devuelve el mensaje de error, o null si el valor es válido.
object Validaciones {

    fun validarNombrePropio(valor: String, campo: String): String? = when {
        valor.isBlank() -> "Debes ingresar tu $campo"
        valor.trim().length < 2 -> "El $campo es demasiado corto"
        !valor.all { it.isLetter() || it == ' ' } -> "El $campo solo puede tener letras"
        else -> null
    }

    fun validarRut(valor: String): String? = when {
        valor.isBlank() -> "Debes ingresar tu RUT"
        !rutTieneDigitoVerificadorCorrecto(valor) -> "RUT inválido (ej: 12.345.678-5)"
        else -> null
    }

    fun validarCorreo(valor: String): String? = when {
        valor.isBlank() -> "Debes ingresar tu correo electrónico"
        !Patterns.EMAIL_ADDRESS.matcher(valor.trim()).matches() -> "Correo electrónico inválido"
        else -> null
    }

    fun validarContrasena(valor: String): String? = when {
        valor.isBlank() -> "Debes ingresar una contraseña"
        valor.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }

    // Verifica el dígito verificador del RUT con el algoritmo módulo 11
    private fun rutTieneDigitoVerificadorCorrecto(rut: String): Boolean {
        val limpio = rut.replace(".", "").replace("-", "").uppercase()
        if (limpio.length < 8 || limpio.length > 9) return false

        val cuerpo = limpio.dropLast(1)
        val digitoIngresado = limpio.last()
        if (!cuerpo.all { it.isDigit() }) return false

        var suma = 0
        var multiplicador = 2
        for (digito in cuerpo.reversed()) {
            suma += digito.digitToInt() * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resto = 11 - (suma % 11)
        val digitoEsperado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }
        return digitoIngresado == digitoEsperado
    }
}