package mx.tec.tickets.ui.screens.admin.ComponentesAdmin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import mx.tec.tickets.model.CommonTicket
import mx.tec.tickets.ui.components.TicketAdminMenu
import mx.tec.tickets.ui.screens.mesa.components.MesaTicketCard   // ← IMPORT CORRECTO


@Composable
fun AdminTicketItem(
    ticket: CommonTicket,
    navController: NavController,
    userID: Int,
    token: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Llamada POSICIONAL (evita fallos por nombres distintos)
        MesaTicketCard(ticket, navController, userID, token)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, end = 6.dp)
                .zIndex(1f),
            contentAlignment = Alignment.TopEnd
        ) {
            TicketAdminMenu()
        }
    }
}
