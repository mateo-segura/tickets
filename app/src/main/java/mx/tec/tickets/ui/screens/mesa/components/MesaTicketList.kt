package mx.tec.tickets.ui.screens.mesa.components

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
import mx.tec.tickets.model.CommonTicket
import org.json.JSONArray

@Composable
fun MesaTicketList(
    navController: NavController, 
    userID: Int,
    token:String,
    refreshKey: Int = 0, // <- Agregar este parámetro,
    selectedCategory: String?,
    selectedPriority: String?,
    selectedDateSort: String
) {
    val context = LocalContext.current
    var Tickets by remember { mutableStateOf(listOf<CommonTicket>()) }

    LaunchedEffect(refreshKey) { // <- Cambiar Unit por refreshKey
        fetchTickets(
            context,
            userID,
            token,
            selectedCategory,
            selectedPriority,
            selectedDateSort
        ) { fetchedTickets ->
            Tickets = fetchedTickets
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
        //.statusBarsPadding()
        // Status bars padding estaba causando padding conflictivo
    ) {
        items(Tickets) { ticket ->
            MesaTicketCard(ticket, navController, userID, token)
        }
    }
}


fun fetchTickets(
    context: Context,
    userID: Int,
    token: String,
    category: String?,
    priority: String?,
    dateSort: String,
    onResult: (List<CommonTicket>) -> Unit
) {
    val tickets = mutableListOf<CommonTicket>()
    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:3000/tickets"
    val metodo = Request.Method.GET

    val queryParams = mutableListOf<String>()

    category?.let{
        queryParams.add("category=$it")
    }

    priority?.let{
        queryParams.add("priority=$it")
    }

    queryParams.add("sort_date=$dateSort")

    val url = if(queryParams.isNotEmpty()){
        "$baseUrl?${queryParams.joinToString ("&")}"
    } else {
        baseUrl
    }

    Log.d("API_URL", "Fetching accepted tickets from: $url")

    val listener = Response.Listener<JSONArray> { response ->
        Log.d("API_RESPONSE", "Filtered Tickets Received: ${response.toString()}")
        for (i in 0 until response.length()) {
            val acceptedTickets = CommonTicket(
                response.getJSONObject(i).getInt("id"),
                response.getJSONObject(i).getString("title"),
                response.getJSONObject(i).getString("description"),
                response.getJSONObject(i).getString("category"),
                response.getJSONObject(i).getString("priority"),
                response.getJSONObject(i).getString("status"),
                response.getJSONObject(i).getInt("created_by"),
                response.getJSONObject(i).optInt("assigned_to", -1), // puede haber nulos
                response.getJSONObject(i).getString("created_at"),
                response.getJSONObject(i).getInt("is_active"),
                response.getJSONObject(i).getInt("acepted"),

                )
            tickets.add(acceptedTickets)
        }
        if (response.length() == 0) {
            Log.w("API_RESPONSE", "Server returned 0 tickets for the current filter.")
        }

        onResult(tickets)
    }
    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message.toString())
    }
    val request = object : JsonArrayRequest(
        metodo, url, null, listener, errorListener
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Bearer $token"
            return headers
        }
    }
    queue.add(request)
}