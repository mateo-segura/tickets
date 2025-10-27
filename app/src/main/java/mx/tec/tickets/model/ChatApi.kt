package mx.tec.tickets.model

import retrofit2.http.*
import retrofit2.Response

data class MessageRequest(
    val ticket_id: Int,
    val sender_user_id: Int,
    val body: String,
    val file_id: Int? = null
)


data class MessageResponse(
    val id: Int,
    val ticket_id: Int,
    val sender_user_id: Int,
    val body: String,
    val created_at: String,
    val file_id: Int? = null
)



interface ChatApi {
    // Obtener mensajes por ticket
    @GET("ticketMessages/byTicket/{ticket_id}")
    suspend fun getMessagesByTicket(
        @Path("ticket_id") ticketId: Int
    ): Response<List<MessageResponse>>

    // Enviar mensaje
    @POST("ticketMessages/insertar")
    suspend fun sendMessage(@Body request: MessageRequest): Response<Unit>
}

