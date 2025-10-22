package mx.tec.tickets.model

data class NotificationItem(
    val id: Int,
    val user_id: Int,
    val type: String,
    val data: NotificationData,
    val isRead: Int,
    val created_at: String
) {
    data class NotificationData(
        val message: String,
        val ticket_id: Int,
        val message_id: Int,
        val sender_user_id: Int
    )
}
