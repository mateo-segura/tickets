package mx.tec.tickets.ui.screens.admin


// mx.tec.tickets.ui.screens.admin.ReassignUserDialog.kt

import androidx.compose.foundation.layout.*   // <- asegúrate que esté
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

// ⬇️ ESTE IMPORT ES EL QUE TE FALTABA
import androidx.compose.foundation.layout.Arrangement

data class TechnicianItem(
    val id: Int,
    val name: String,
    val assignedCount: Int
)

@Composable
fun ReassignUserDialog(
    ticketTitle: String,
    description: String,
    // ⬇️ ARREGLADO: ya no List<Unit>, ahora List<TechnicianItem>
    technicians: List<TechnicianItem>,
    onAssign: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.large, tonalElevation = 4.dp) {
            Column(modifier = Modifier.padding(20.dp).widthIn(min = 320.dp, max = 460.dp)) {

                Text("Título Ticket", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(ticketTitle, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))

                Text("Description", style = MaterialTheme.typography.labelLarge)
                Text(description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))

                Divider()
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .heightIn(min = 120.dp, max = 360.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(technicians, key = { it.id }) { tech ->
                        TechnicianRow(
                            tech = tech,
                            onAssign = { onAssign(tech.id) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDismiss
                ) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun TechnicianRow(
    tech: TechnicianItem,
    onAssign: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tech.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Tickets asignados: ${tech.assignedCount}", style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = onAssign) { Text("Asignar") }
        }
    }
}
