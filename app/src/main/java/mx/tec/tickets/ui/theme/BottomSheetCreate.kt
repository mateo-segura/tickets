package mx.tec.tickets.ui.theme

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.ui.screens.SpinnerDropDown
import org.json.JSONObject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCreate(
    showSheetCreate: Boolean,
    new : Boolean,
    jwtToken: String,
    context: Context,
    onDismiss: () -> Unit,
    userID:Int,
    onTicketCreated: () -> Unit = {} // <- Agregar este parámetro
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // optional, prevents half-expanded state
    )
    val opcionesPrioridad = listOf("ALTA", "MEDIA", "BAJA")
    val opcionesCategoria = listOf("SOFTWARE", "HARDWARE", "REDES", "OTRO")
    var tecnicosEmails by remember { mutableStateOf<List<String>>(emptyList()) }
    var tecnicos by remember { mutableStateOf<List<Tecnico>>(emptyList()) }
    var selectedTecnicoId by remember { mutableStateOf<Int?>(null) }
    var selectedTecnicoEmail by remember { mutableStateOf("Asignar técnico") }

    LaunchedEffect(Unit) {
        fetchTecnicos(jwtToken, context) { lista ->
            tecnicos = lista
        }
    }
    val opcionesTecnico = tecnicos.map { it.email }
    // Bottom sheet itself
    if (showSheetCreate) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            var titleText by remember { mutableStateOf("") }
            var descriptionText by remember { mutableStateOf("") }
            var initStatePrioridad by remember { mutableStateOf("ALTA") }
            var initStateCategoria by remember { mutableStateOf("REDES") }
            var initStateTecnico by remember { mutableStateOf("Asignar técnico") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text(text = "Titulo",
                        style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 4.dp, vertical = 16.dp)
                        .border(width = 1.dp, color = Color.Black),
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Escribe la descripción") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                
                // Prioridad con ancho completo
                SpinnerDropDownFullWidth("Prioridad", initStatePrioridad, opcionesPrioridad) { 
                    initStatePrioridad = it 
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Categoría con ancho completo
                SpinnerDropDownFullWidth("Categoría", initStateCategoria, opcionesCategoria) { 
                    initStateCategoria = it 
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Spinner de Técnico usa todo el ancho
                SpinnerDropDownFullWidth(
                    texto = "Técnico",
                    seleccion = selectedTecnicoEmail,
                    opciones = opcionesTecnico
                ) { emailSeleccionado ->
                    selectedTecnicoEmail = emailSeleccionado
                    // Buscar el ID correspondiente
                    val tecnico = tecnicos.find { it.email == emailSeleccionado }
                    selectedTecnicoId = tecnico?.id
                }
                Spacer(modifier = Modifier.height(10.dp))

                val assignedToId = if (selectedTecnicoEmail == "Asignar técnico") {
                    null
                } else {
                    selectedTecnicoId
                }
                
                // Botón Crear centrado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (titleText.isBlank() || descriptionText.isBlank()) {
                                return@Button
                            }

                            val assignedToId = if (selectedTecnicoEmail == "Asignar técnico") {
                                null
                            } else {
                                selectedTecnicoId
                            }

                            crearTicket(
                                jwtToken = jwtToken,
                                context = context,
                                title = titleText,
                                description = descriptionText,
                                category = initStateCategoria,
                                priority = initStatePrioridad,
                                created_by = userID,
                                assigned_to = assignedToId,
                                onSuccess = {
                                    onTicketCreated() // <- Llamar al callback
                                    onDismiss()
                                },
                                onError = { errorMsg ->
                                    println("Error al crear ticket: $errorMsg")
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Crear")
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ShowBottomSheetCreate() {

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerDropDown(
    texto: String,
    seleccion: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit,
){
    var abierto by remember { mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = abierto,
        onExpandedChange = { abierto = !abierto }
    ) {
        TextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(texto)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(abierto) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .width(140.dp)
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEach{ opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        abierto = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerDropDownFullWidth(
    texto: String,
    seleccion: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit,
){
    var abierto by remember { mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = abierto,
        onExpandedChange = { abierto = !abierto }
    ) {
        TextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(texto)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(abierto) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEach{ opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        abierto = false
                    }
                )
            }
        }
    }
}

data class Tecnico(val id: Int, val email: String)

fun fetchTecnicos(
    jwtToken: String,
    context: Context,
    onSuccess: (List<Tecnico>) -> Unit
) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/usuarios/tecnicos"

    val request = object : JsonArrayRequest(
        Method.GET, url, null,
        { response ->
            val tecnicos = mutableListOf<Tecnico>()
            for (i in 0 until response.length()) {
                val user = response.getJSONObject(i)
                if (user.getString("role") == "TECNICO" && user.getInt("is_active") == 1) {
                    tecnicos.add(
                        Tecnico(
                            id = user.getInt("id"),
                            email = user.getString("email")
                        )
                    )
                }
            }
            onSuccess(tecnicos)
        },
        { error ->
            println("Error: ${error.message}")
            onSuccess(emptyList())
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            return HashMap<String, String>().apply {
                put("Authorization", "Bearer $jwtToken")
                put("Content-Type", "application/json")
            }
        }
    }
    queue.add(request)
}

fun crearTicket(
    jwtToken: String,
    context: Context,
    title: String,
    description: String,
    category: String,
    priority: String,
    created_by: Int,
    assigned_to: Int?,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/tickets/insertar"

    val jsonBody = JSONObject().apply {
        put("title", title)
        put("description", description)
        put("category", category)
        put("priority", priority)
        put("created_by", created_by)
        put("assigned_to", assigned_to) // Volley acepta null y enteros
    }

    val request = object : JsonObjectRequest(
        Request.Method.POST, url, jsonBody,
        { response -> onSuccess() },
        { error ->
            val errorMsg = error.networkResponse?.statusCode?.toString()
                ?: error.message ?: "Error desconocido"
            onError(errorMsg)
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            return HashMap<String, String>().apply {
                put("Authorization", "Bearer $jwtToken")
                put("Content-Type", "application/json")
            }
        }
    }
    queue.add(request)
}