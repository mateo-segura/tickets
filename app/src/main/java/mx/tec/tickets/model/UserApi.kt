package mx.tec.tickets.model


import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // Lista de técnicos activos
    @GET("usuarios/tecnicos")
    fun getTechnicians(
        @Header("Authorization") token: String
    ): Call<List<UserResponse>>

    // Reasignar ticket a técnico
    @PATCH("usuarios/asignar")
    fun assignTicket(
        @Header("Authorization") token: String,
        @Body body: AssignTicketRequest
    ): Call<Unit>
}

// ===== Modelos mínimos =====
data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val is_active: Int
)

data class AssignTicketRequest(
    val ticket_id: Int,
    val user_id: Int
)
