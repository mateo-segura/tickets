package mx.tec.tickets.ui.screens.tecnico.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.model.NonAcceptedTicket
import org.json.JSONArray

@Composable
fun TecnicoNonAcceptedTicketList(
    navController: NavController,
    userID: Int,
    token:String,
    onTicketAcceptedGlobally: () -> Unit
) {
    val context = LocalContext.current
    var nonAceptedTickets by remember { mutableStateOf(listOf<NonAcceptedTicket>()) }
    println("userID from ticketlist ${userID}")
    LaunchedEffect(Unit) {
        fetchNonAceptedTickets(context, userID,token) { fetchedTickets ->
            nonAceptedTickets = fetchedTickets
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
        //.statusBarsPadding()
        // Status bars padding estaba causando padding conflictivo
    ) {
        items(nonAceptedTickets, key = { it.ticketID }) { ticket ->
            TecnicoNonAcceptedTicketCard(ticket, navController, userID, token,
                onTicketAccepted = { acceptedTicket ->
                    nonAceptedTickets = nonAceptedTickets.filter {
                        it.ticketID != acceptedTicket.ticketID
                    }

                    onTicketAcceptedGlobally()
                },

                onTicketRejected = { rejectedTicket ->
                    // Do the same for rejection
                    nonAceptedTickets = nonAceptedTickets.filter {
                        it.ticketID != rejectedTicket.ticketID
                    }
                }
            )
        }
    }
}


fun fetchNonAceptedTickets(
    context: Context,
    userID: Int,
    token: String,
    onResult: (List<NonAcceptedTicket>) -> Unit
) {
    val nonAcceptedTickets = mutableListOf<NonAcceptedTicket>()
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/tickets/nonAceptedTickets/${userID}"
    val metodo = Request.Method.GET
    val listener = Response.Listener<JSONArray> { response ->
        for (i in 0 until response.length()) {
            val nonAcceptedTicket = NonAcceptedTicket(
                response.getJSONObject(i).getInt("Ticket_ID"),
                response.getJSONObject(i).getString("title"),
                response.getJSONObject(i).getString("description"),
                response.getJSONObject(i).getString("category"),
                response.getJSONObject(i).getString("priority"),
                response.getJSONObject(i).getString("status"),
                response.getJSONObject(i).getInt("acepted"),
                response.getJSONObject(i).getString("created_at")
            )
            nonAcceptedTickets.add(nonAcceptedTicket)
        }
        onResult(nonAcceptedTickets)
    }
    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message.toString())
    }
    val request = object : JsonArrayRequest(metodo, url, null, listener, errorListener) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer $token" // Ajusta si tu backend usa otro formato
            return headers
        }
    }

    queue.add(request)
}
