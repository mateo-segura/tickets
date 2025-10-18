package mx.tec.tickets.model

//Anadir implementacion despues
data class LoginResponse(
    var id: String,
    var token: String,
    var role:String,
    var isActive:Int
)