package mx.tec.tickets.ui.screens

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import mx.tec.tickets.ui.TicketList

//viewmodel area compartida de todas las pantallas
@Preview
@Composable
fun MenuScreen(navController: NavController) {
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
                    0 -> HomeScreen(navController)
                    1 -> TicketsScreen()
                    2 -> NotificationsScreen(navController)
                }
            }

        }
}



@Composable
fun HomeScreen(navController: NavController) {
    MainTecnicoScreen(navController)
}

@Composable
fun TicketsScreen() {

}

@Composable
fun NotificationsScreen(navController:NavController) {
    TicketList(navController)
}