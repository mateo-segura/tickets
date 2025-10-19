package mx.tec.tickets.model

//todo: refactorizar a Ticket
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
