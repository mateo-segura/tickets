package mx.tec.tickets.ui.screens.tecnico


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.tickets.ui.screens.tecnico.components.NotificationList
import mx.tec.tickets.ui.theme.drawColoredShadow

@Composable
fun MainNotificationScreen(navController: NavController, userID: Int) {
    var showFilterType by remember { mutableStateOf(false) }
    var showFilterRead by remember { mutableStateOf(false) }
    var showFilterDate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Header principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawColoredShadow(
                    color = Color.Black,
                    alpha = 0.25f,
                    borderRadius = 8.dp,
                    shadowRadius = 12.dp,
                    offsetY = 4.dp,
                    offsetX = 0.dp
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Notificaciones",
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }

        // Botones de filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val buttonWidth = 120.dp
            val buttonHeight = 35.dp

            Button(
                onClick = { showFilterType = true },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth, buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,
                        alpha = 0.25f,
                        borderRadius = 8.dp,
                        shadowRadius = 12.dp,
                        offsetY = 4.dp,
                        offsetX = 0.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Tipo")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tipo", fontSize = 12.sp)
            }

            Button(
                onClick = { showFilterRead = true },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth, buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,
                        alpha = 0.25f,
                        borderRadius = 8.dp,
                        shadowRadius = 12.dp,
                        offsetY = 4.dp,
                        offsetX = 0.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Leídas")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Leídas", fontSize = 12.sp)
            }

            Button(
                onClick = { showFilterDate = true },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth, buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,
                        alpha = 0.25f,
                        borderRadius = 8.dp,
                        shadowRadius = 12.dp,
                        offsetY = 4.dp,
                        offsetX = 0.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Fecha")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fecha", fontSize = 12.sp)
            }
        }

        // Lista de notificaciones
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            NotificationList(navController, userID)
        }
    }
}
