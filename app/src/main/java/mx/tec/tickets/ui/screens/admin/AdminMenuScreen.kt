package mx.tec.tickets.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

// Import que ya existía y NO toco
import mx.tec.tickets.ui.screens.admin.adminUsersScreen.MainAdminUserScreen

import mx.tec.tickets.ui.screens.mesa.MainAdminScreen
import mx.tec.tickets.ui.screens.tecnico.MainTecnicoScreen

// CAMBIO: import de tu NUEVA pantalla de usuarios
import mx.tec.tickets.ui.screens.admin.MainAdminUserScreenNew

@Composable
fun AdminMenuScreen(navController: NavController, token: String, role: String, userID: Int) {
    var selectedOption by remember { mutableIntStateOf(0) }
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
                    icon = { Icon(Icons.Default.Lock, contentDescription = "Tickets Cerrados") },
                    label = { Text("Tickets Cerrados") }
                )
                NavigationBarItem(
                    selected = selectedOption == 2,
                    onClick = { selectedOption = 2 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Settings") },
                    label = { Text("Notificaciones") }
                )
                NavigationBarItem(
                    selected = selectedOption == 3,
                    onClick = { selectedOption = 3 },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Usuarios") },
                    label = { Text("Usuarios") }
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
                0 -> HomeScreen(navController, token, role, userID)
                1 -> TecnicoClosedTicketsScreen()
                2 -> UsersScreen(navController, token, role, userID) // mantiene el flujo original
                3 -> NotificationsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, token: String, role: String, userID: Int) {
    MainAdminScreen(navController, token, role, userID)
}

@Composable
fun TecnicoClosedTicketsScreen() {
    // pendiente
}

@Composable
fun UsersScreen (navController: NavController, token: String, role: String, userID: Int) {
    // CAMBIO: antes se llamaba a MainAdminUserScreen (vieja).
    // Ahora pintamos tu NUEVA lista de usuarios sin tocar nada más.
    MainAdminUserScreenNew(navController = navController, token = token)

    // Si quieres mantener la vieja como fallback por cualquier cosa, déjala comentada:
    // MainAdminUserScreen(navController, token, role, userID)
}
@Composable
fun NotificationsScreen (){

}