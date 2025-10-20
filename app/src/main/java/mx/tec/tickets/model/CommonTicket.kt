package mx.tec.tickets.model

data class CommonTicket (
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val createdBy: Int,
    val assignedTo: Int,
    val createdAt: String,
    val isActive: Int,
    val accepted: Int,
)