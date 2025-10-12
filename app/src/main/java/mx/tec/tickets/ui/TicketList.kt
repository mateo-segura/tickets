package mx.tec.tickets.ui

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
import mx.tec.tickets.model.Ticket
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun TicketList(navController: NavController) {
    val context = LocalContext.current
    var tickets by remember { mutableStateOf(listOf<Ticket>()) }

    LaunchedEffect(Unit) {
        fetchTickets(context) { fetchedTickets ->
            tickets = fetchedTickets
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
        //.statusBarsPadding()
        // Status bars padding estaba causando padding conflictivo
    ) {
        items(tickets) { ticket ->
            TicketCard(ticket, navController)
        }
    }
}

//Consumir el API, Recibe: ?, Devuelve: List<User>
fun fetchTickets(context: Context, onResult: (List<Ticket>) -> Unit) {
    val tickets = mutableListOf<Ticket>()
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/tickets"
    val metodo = Request.Method.GET
    val listener = Response.Listener<JSONArray> { response ->
        for (i in 0 until response.length()) {
            val ticket = Ticket(
                response.getJSONObject(i).getString("title"),
                response.getJSONObject(i).getString("priority"),
                response.getJSONObject(i)
                    .getIntOrNull("assigned_to"), //todo: assignedTo puede ser nulo? (en la base de datos hay uno null)
                response.getJSONObject(i).getString("category"),
                response.getJSONObject(i).getString("created_at"),
                response.getJSONObject(i).getString("description"),

                )
            tickets.add(ticket)
        }
        onResult(tickets)
    }
    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message.toString())
    }
    val request = JsonArrayRequest(
        metodo, url,
        null, listener, errorListener
    )
    queue.add(request)
}

fun JSONObject.getIntOrNull(key: String): Int? =
    if (isNull(key)) null else getInt(key)