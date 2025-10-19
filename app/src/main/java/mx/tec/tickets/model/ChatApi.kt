package mx.tec.tickets.model

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class MessageRequest(
    val ticket_id: Int,
    val sender_user_id: Int,
    val body: String
)

interface ChatApi {
    @POST("ticketMessages/insertar")
    suspend fun sendMessage(@Body request: MessageRequest): Response<Unit>
}
