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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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

//viewmodel area compartida de todas las pantallas
@Preview
@Composable
fun MenuScreen() {
    var selectedOption by remember { mutableIntStateOf(0) }
    var navBarSize by remember { mutableStateOf(0.dp)}
    val density = LocalDensity.current

    Box(){
        Scaffold(
            modifier = Modifier
                .height(120.dp)
                .onGloballyPositioned { coordinates ->
                    navBarSize = with(density) { coordinates.size.height.toDp() }
                },

            bottomBar = {
                BottomAppBar (
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ){
                    NavigationBarItem(
                        selected = selectedOption == 0,
                        onClick = { selectedOption = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Inicio") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
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

        LargeFloatingActionButton (
            onClick = { selectedOption = 1 },
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .offset(y = (-navBarSize.value/1.5f).dp)
                .statusBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Icon(Icons.Default.Create, contentDescription = "Editar",
                    modifier = Modifier
                        .padding(bottom = 4.dp))
                Text("Editar")
            }
        }
    }
}



@Composable
fun HomeScreen() {
}

@Composable
fun TicketsScreen() {
}

@Composable
fun NotificationsScreen() {
}