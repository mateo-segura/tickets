package mx.tec.tickets.model

data class NonAcceptedTicket (
    val ticketID: Int,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val accepted: Int,
    val createdAt: String
)
