package mx.tec.tickets.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyCategoryPriorityDialog(
    ticketTitle: String,
    description: String,
    categories: List<String>,
    priorities: List<String>,
    selectedCategoryInitial: String?,
    selectedPriorityInitial: String?,
    onConfirm: (newCategory: String, newPriority: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var category by remember { mutableStateOf(selectedCategoryInitial ?: categories.firstOrNull().orEmpty()) }
    var priority by remember { mutableStateOf(selectedPriorityInitial ?: priorities.firstOrNull().orEmpty()) }

    var catExpanded by remember { mutableStateOf(false) }
    var priExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(min = 300.dp, max = 420.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Título Ticket", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(ticketTitle, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))

                Text("Description", style = MaterialTheme.typography.labelLarge)
                Text(description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Categoría
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Categoría", style = MaterialTheme.typography.labelLarge)
                        ExposedDropdownMenuBox(
                            expanded = catExpanded,
                            onExpandedChange = { catExpanded = !catExpanded }
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Categoría") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                                singleLine = true,   // ← evita saltos de línea
                                maxLines = 1         // ← fuerza una sola línea
                            )
                            ExposedDropdownMenu(
                                expanded = catExpanded,
                                onDismissRequest = { catExpanded = false }
                            ) {
                                categories.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            category = option
                                            catExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Prioridad
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Prioridad", style = MaterialTheme.typography.labelLarge)
                        ExposedDropdownMenuBox(
                            expanded = priExpanded,
                            onExpandedChange = { priExpanded = !priExpanded }
                        ) {
                            OutlinedTextField(
                                value = priority,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Prioridad") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priExpanded) },
                                singleLine = true,   // ← idem
                                maxLines = 1
                            )
                            ExposedDropdownMenu(
                                expanded = priExpanded,
                                onDismissRequest = { priExpanded = false }
                            ) {
                                priorities.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            priority = option
                                            priExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    ) { Text("Cancelar") }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirm(category, priority) }
                    ) { Text("Aceptar") }
                }
            }
        }
    }
}
