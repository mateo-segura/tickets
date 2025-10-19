package mx.tec.tickets.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import mx.tec.tickets.ui.TicketList

//viewmodel area compartida de todas las pantallas
@Preview
@Composable
fun TecnicoMenuScreen(navController: NavController,token: String,role: String,userID: Int) {
    var selectedOption by remember { mutableIntStateOf(0) }
        Scaffold(
            bottomBar = {
                BottomAppBar{
                    NavigationBarItem(
                        selected = selectedOption == 0,
                        onClick = { selectedOption = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Inicio") }
                    )
                    //Spacer(modifier = Modifier.weight(1f))

                    FloatingActionButton(
                        onClick = { /* do something */ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }

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
                    0 -> HomeScreen(navController,token,role,userID)
                    1 -> TicketsScreen()
                    2 -> NotificationsScreen(navController)
                }
            }

        }
}



@Composable
fun HomeScreen(navController: NavController,token: String,role: String,userID: Int) {
    MainTecnicoScreen(navController,token,role,userID)
}

@Composable
fun TicketsScreen() {

}

@Composable
fun NotificationsScreen(navController:NavController) {
}