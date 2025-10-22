package mx.tec.tickets.ui.screens.admin.Users


data class AppUser(
    val id: Int,
    val email: String,
    val username: String?,
    val role: String
) {
    val displayName: String
        get() = when {
            !username.isNullOrBlank() -> username
            email.contains("@") -> email.substringBefore("@")
            else -> email
        }
}
