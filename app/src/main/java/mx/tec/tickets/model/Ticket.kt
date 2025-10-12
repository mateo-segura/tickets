package mx.tec.tickets.model

data class Ticket(
    var title: String,
    var priority: String,
    var assignedTo: Int?, //todo: assignedTo puede ser nulo? (en la base de datos hay uno null)
    var category: String,
    var createdAt: String,
    var description: String,
)