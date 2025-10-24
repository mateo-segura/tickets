package mx.tec.tickets.model

import retrofit2.Call
import retrofit2.http.*

// GET por id ya documentado
interface TicketApi {
    @GET("tickets/{id}")
    fun getTicketById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<TicketResponse>

    // PATCH: asignar categoría
    @PATCH("tickets/asigCategoria")
    fun patchCategory(
        @Header("Authorization") token: String,
        @Body body: PatchCategoryRequest
    ): Call<Unit>

    // PATCH: asignar prioridad
    @PATCH("tickets/asigPrioridad")
    fun patchPriority(
        @Header("Authorization") token: String,
        @Body body: PatchPriorityRequest
    ): Call<Unit>
}

// ====== modelos mínimos ======
data class TicketResponse(
    val id: Int,
    val title: String,
    val description: String,
    val category: String?,
    val priority: String?
)

data class PatchCategoryRequest(
    val id: Int,
    val category: String    // valores válidos: REDES | SOFTWARE | HARDWARE | OTRO
)

data class PatchPriorityRequest(
    val id: Int,
    val priority: String    // valores válidos: BAJA | MEDIA | ALTA
)
