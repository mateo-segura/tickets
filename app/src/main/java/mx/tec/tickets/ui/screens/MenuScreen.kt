package mx.tec.tickets.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

//viewmodel area compartida de todas las pantallas
@Composable
fun MenuScreen() {
    var selectedOption by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    selected = selectedOption == 0,
                    onClick = { selectedOption = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedOption == 1,
                    onClick = { selectedOption = 1 },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Tickets") },
                    label = { Text("Crear") }
                )
                NavigationBarItem(
                    selected = selectedOption == 2,
                    onClick = { selectedOption = 2 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Settings") },
                    label = { Text("Notificaciones") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedOption) {
                0 -> HomeScreen()
                1 -> TicketsScreen()
                2 -> NotificationsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Text("Inicio")
}

@Composable
fun TicketsScreen() {
    Text("Crear tickets")
}

@Composable
fun NotificationsScreen() {
    Text("NotificationsScreen")
}