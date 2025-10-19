package mx.tec.tickets.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import mx.tec.tickets.model.Ticket

@Composable
fun TicketCard(ticket: Ticket, navController: NavController) {
    val padding = 10.dp
    val fontSizeNormal = 11.sp
    val fontSizeTitle = 14.sp

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                val jsonTicket = Gson().toJson(ticket)
                navController.navigate("detalle/${jsonTicket}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp), // Padding externo, no en cada columna
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f) // Ocupa proporcionalmente el espacio
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "${ticket.title.take(13)}...",
                    fontSize = fontSizeTitle,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Prioridad: ${ticket.priority}",
                    fontSize = fontSizeNormal,
                )
                Text(
                    text = "Técnico: ${ticket.assignedTo}",
                    fontSize = fontSizeNormal,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center
                ) {
//                    Text(
//                        "Descripción",
//                        fontSize = fontSizeNormal
//                    )
                    Text(
                        "Categoría: ${ticket.category}",
                        fontSize = fontSizeNormal
                    )
                    Spacer(Modifier.size(padding))
                    Text(
                        ticket.createdAt.take(10),
                        fontSize = fontSizeNormal,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.size(padding))
                Text(
                    ticket.description,
                    fontSize = fontSizeNormal,
                    style = LocalTextStyle.current.merge( /*Estilo del párrafo: https://developer.android.com/develop/ui/compose/text/style-paragraph*/
                        TextStyle(
                            lineHeight = 1.0.em,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                        )
                    ),
                    maxLines = 3, //https://www.develou.com/cards-en-jetpack-compose/
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
