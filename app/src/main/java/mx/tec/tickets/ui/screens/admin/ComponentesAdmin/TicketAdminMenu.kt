package mx.tec.tickets.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Menú de 3 puntos visible para ADMIN.
 * Desde aquí se navega hacia:
 * 1. Modificar categoría/prioridad
 * 2. Reasignar técnico
 */
@Composable
fun TicketAdminMenu(
    navController: NavController,
    ticketId: Int, // para que sepas a qué ticket se está aplicando
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Acciones de administrador"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Mod. Categoría/Prioridad") },
                onClick = {
                    expanded = false
                    // Ruta hacia tu pantalla ModifyCategoryPriorityDialog
                    navController.navigate("modifyCategoryPriority/$ticketId")
                }
            )

            DropdownMenuItem(
                text = { Text("Reasignar Técnico") },
                onClick = {
                    expanded = false
                    // Ruta hacia tu pantalla ReassignUserDialog
                    navController.navigate("reassignUser/$ticketId")
                }
            )
        }
    }
}
