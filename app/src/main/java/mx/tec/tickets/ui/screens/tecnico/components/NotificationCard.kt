package mx.tec.tickets.ui.screens.tecnico.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import mx.tec.tickets.model.NotificationItem

@Composable
fun NotificationCard(
    notification: NotificationItem,
    navController: NavController
) {
    val jsonNotification = Gson().toJson(notification)
    val fontSizeTitle = 14.sp
    val fontSizeNormal = 11.sp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate("notificationDetail/${jsonNotification}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead == 0) Color(0xFFE3F2FD) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = notification.data.message,
                fontSize = fontSizeTitle,
                fontWeight = if (notification.isRead == 0) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "Ticket ID: ${notification.data.ticket_id}",
                fontSize = fontSizeNormal
            )
            Text(
                text = "Fecha: ${notification.created_at.take(10)}",
                fontSize = fontSizeNormal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationCard() {
    val mockNotification = NotificationItem(
        id = 2,
        user_id = 2,
        type = "NEW_MESSAGE",
        data = NotificationItem.NotificationData(
            message = "Se agregó un nuevo mensaje en tu ticket",
            ticket_id = 1,
            message_id = 14,
            sender_user_id = 3
        ),
        isRead = 0,
        created_at = "2025-10-20T19:49:31.000Z"
    )
    NotificationCard(notification = mockNotification, navController = rememberNavController())
}