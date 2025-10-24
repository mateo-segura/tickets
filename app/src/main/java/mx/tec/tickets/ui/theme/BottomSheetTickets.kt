package mx.tec.tickets.ui.theme

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import mx.tec.tickets.model.NonAcceptedTicket
import mx.tec.tickets.model.Ticket
import mx.tec.tickets.ui.screens.fetchTicket
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTickets(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    navController: NavController,
    userID: Int,
    jsonTicket: String,
    nonAcceptedTicket: NonAcceptedTicket,
    new : Boolean,
    jwtToken: String,
    onTicketAccepted: () -> Unit,
    onTicketRejected: () -> Unit

) {
    val context = LocalContext.current
    // val (ticket, setTicket) = remember { mutableStateOf<Ticket?>(null)}

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // optional, prevents half-expanded state
    )
    /*
    LaunchedEffect(Unit){
        fetchTicket(context) { result ->
            setTicket(result)
        }
    }
     */

    // Bottom sheet itself
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Titulo: ${nonAcceptedTicket.title}",
                    style = MaterialTheme.typography.titleLarge)

                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .background(Color.Red) // Cambiar color
                            .size(20.dp)
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Prioridad - ${nonAcceptedTicket.priority}"
                    )
                }
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .background(Color.Green) // Cambiar el color
                            .size(20.dp)
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Categoria - ${nonAcceptedTicket.category}"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    text = "Descripcion",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 4.dp, vertical = 16.dp)
                ){
                    Text(
                        text = nonAcceptedTicket.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!new) {
                    // Boton de edit
                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            onClick = { navController.navigate("detailnonaccepted/${jsonTicket}?token=$jwtToken") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )) {
                            Text("Editar")
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            onClick = { acceptTicket(context, jwtToken, nonAcceptedTicket.ticketID, {
                                onTicketAccepted()
                                onTicketRejected()
                            }) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )) {
                            Text("Aceptar")
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            onClick = { rejectTicket(context, jwtToken, nonAcceptedTicket.ticketID, {
                                onTicketAccepted()
                                onTicketRejected()
                            }) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )) {
                            Text("Rechazar")
                        }
                    }
                }


            }
        }
    }
}

fun acceptTicket(context: Context, jwtToken: String, ticketId: Int, onSuccess: () -> Unit) {
    val url = "http://10.0.2.2:3000/tickets/aceptarTicket" // Adjust to your route if needed
    val queue = Volley.newRequestQueue(context)

    val jsonBody = JSONObject()
    jsonBody.put("id", ticketId)

    val request = object : JsonObjectRequest(
        Request.Method.PATCH,
        url,
        jsonBody,
        Response.Listener { response ->
            Log.d("ACEPTAR_TICKET", "Ticket aceptado exitosamente: $response")
            onSuccess()
        },
        Response.ErrorListener { error ->
            Log.e("ACEPTAR_TICKET", "Error al aceptar ticket: ${error.message}")
            Log.e("ACEPTAR_TICKET", "$ticketId")
            error.networkResponse?.let {
                Log.e("ACEPTAR_TICKET", "Status code: ${it.statusCode}")
            }
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["Authorization"] = "Bearer $jwtToken" // Only if your backend uses JWT
            return headers
        }
    }

    queue.add(request)
}

fun rejectTicket(context: Context, jwtToken: String, ticketId: Int, onSuccess: () -> Unit) {
    val url = "http://10.0.2.2:3000/tickets/rechazarTicket" // Adjust to your route if needed
    val queue = Volley.newRequestQueue(context)

    val jsonBody = JSONObject()
    jsonBody.put("id", ticketId)

    val request = object : JsonObjectRequest(
        Request.Method.PATCH,
        url,
        jsonBody,
        Response.Listener { response ->
            Log.d("RECHAZAR_TICKET", "Ticket rechazado exitosamente: $response")
            onSuccess()
        },
        Response.ErrorListener { error ->
            Log.e("RECHAZAR_TICKET", "Error al rechazar ticket: ${error.message}")
            Log.e("RECHAZAR_TICKET", "$ticketId")
            error.networkResponse?.let {
                Log.e("RECHAZAR_TICKET", "Status code: ${it.statusCode}")
            }
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["Authorization"] = "Bearer $jwtToken" // Only if your backend uses JWT
            return headers
        }
    }

    queue.add(request)
}