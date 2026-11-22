package com.duoc.agriserv.repository

import kotlin.text.iterator

// Reglas de validación de los formularios.
// Cada función devuelve el mensaje de error, o null si el valor es válido.
object Validaciones {

    // Misma expresión que android.util.Patterns.EMAIL_ADDRESS; Patterns no existe en las pruebas locales
    private val REGEX_CORREO = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

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
        !REGEX_CORREO.matches(valor.trim()) -> "Correo electrónico inválido"
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

    fun validarSuperficie(valor: String): String? {
        val numero = valor.replace(',', '.').toDoubleOrNull()
        return when {
            valor.isBlank() -> "Debes ingresar la superficie"
            numero == null -> "La superficie debe ser un número"
            numero <= 0 -> "La superficie debe ser mayor que 0"
            numero > 10000 -> "La superficie no puede superar las 10.000 hectáreas"
            else -> null
        }
    }

    // Para los campos donde hay que elegir una opción (producto, agua, momento de aplicación)
    fun validarSeleccion(seleccion: Any?, campo: String): String? =
        if (seleccion == null) "Debes elegir $campo" else null

    fun validarComentario(valor: String): String? =
        if (valor.length > 300) "El comentario no puede superar los 300 caracteres" else null

    // El precio es un entero en pesos chilenos, sin puntos ni comas
    fun validarPrecio(valor: String): String? {
        val numero = valor.toLongOrNull()
        return when {
            valor.isBlank() -> "Debes ingresar el precio"
            !valor.all { it.isDigit() } -> "El precio debe ser un número entero, sin puntos ni comas"
            numero == null || numero <= 0 -> "El precio debe ser mayor que 0"
            numero > 100_000_000 -> "El precio no puede superar los $100.000.000"
            else -> null
        }
    }

    fun validarUbicacion(latitud: Double?, longitud: Double?): String? =
        if (latitud == null || longitud == null) "Debes marcar la ubicación del terreno en el mapa" else null
}

//QUE HACE ESTA CLASE?
//Esta clase es la que ayudara a los view model a validar los datos de los campos que esten en los
//Screens segun nuestra logica o reglas de negocio