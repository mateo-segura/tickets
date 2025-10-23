package mx.tec.tickets.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Menú de 3 puntos visible para ADMIN.
 * Anclado al ícono: el DropdownMenu vive en el mismo Box que el IconButton.
 */
@Composable
fun TicketAdminMenu(
    modifier: Modifier = Modifier,
    onModifyCategoryPriority: () -> Unit = {},
    onReassignTech: () -> Unit = {},
    onDownloadReport: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    // El menú queda ANCLADO a este Box. Nada de flotar por la galaxia.
    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Acciones de administrador"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            // Si lo ves un pelín corrido, ajusta el offset. Cero drama.
            offset = DpOffset(x = 0.dp, y = 0.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Mod. Categoría/Prioridad") },
                onClick = {
                    expanded = false
                    onModifyCategoryPriority()
                }
            )
            DropdownMenuItem(
                text = { Text("Reasignar Técnico") },
                onClick = {
                    expanded = false
                    onReassignTech()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                        tint = LocalContentColor.current
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("Descargar reporte") },
                onClick = {
                    expanded = false
                    onDownloadReport()
                }
            )
        }
    }
}
