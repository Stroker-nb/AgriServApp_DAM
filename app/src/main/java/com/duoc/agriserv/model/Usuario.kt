package com.duoc.agriserv.model

open class Usuario
    (
     val idUsuario: Int ,
     var nombreUsuario : String,
     var apellidoUsuario : String ,
     val rutUsuario : Int,
     var correoUsuario : String
            )
    {

    }

class Solicitante
        (
          idUsuario : Int ,
          nombreUsuario : String ,
         apellidoUsuario : String ,
         rutUsuario : Int ,
         correoUsuario : String,
         var solicitudes : MutableList<Solicitudes>
    ) : Usuario (idUsuario  ,
        nombreUsuario ,
        apellidoUsuario ,
        rutUsuario ,
        correoUsuario )
    {

    }
class Ofertante
        (
        idUsuario : Int ,
        nombreUsuario : String ,
        apellidoUsuario : String ,
        rutUsuario : Int ,
        correoUsuario : String,
        var ofertas: MutableList<Ofertas>
) : Usuario (idUsuario  ,
        nombreUsuario ,
        apellidoUsuario ,
        rutUsuario ,
        correoUsuario )
    {

    }