package mx.tec.tickets.ui.screens.tecnico.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import mx.tec.tickets.model.NotificationItem

@Composable
fun NotificationDetail(notificationJson: String, navController: NavController) {
    val notification = Gson().fromJson(notificationJson, NotificationItem::class.java)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Detalle de Notificación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Text(text = "Mensaje: ${notification.data.message}")
            Text(text = "Ticket ID: ${notification.data.ticket_id}")
            Text(text = "Enviado por: ${notification.data.sender_user_id}")
            Text(text = "Tipo: ${notification.type}")
            Text(text = "Leída: ${if (notification.isRead == 1) "Sí" else "No"}")
            Text(text = "Fecha: ${notification.created_at.take(19)}")
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
    }
}