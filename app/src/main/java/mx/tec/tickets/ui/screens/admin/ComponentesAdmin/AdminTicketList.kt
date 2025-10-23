package mx.tec.tickets.ui.screens.admin.ComponentesAdmin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
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
fun AdminTicketList(
    navController: NavController,
    userID: Int,
    token: String,
    refreshKey: Int,
    selectedCategory: String?,
    selectedPriority: String?,
    selectedDateSort: String
) {
    val context = LocalContext.current
    var tickets by remember { mutableStateOf(listOf<CommonTicket>()) }

    LaunchedEffect(refreshKey) {
        fetchTicketsForAdmin(
            context,
            token,
            selectedCategory,
            selectedPriority,
            selectedDateSort
        ) { fetchedTickets ->
            tickets = fetchedTickets
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tickets, key = { it.id }) { t ->
            AdminTicketItem(
                ticket = t,
                navController = navController,
                userID = userID,
                token = token
            )
        }
    }
}

private fun fetchTicketsForAdmin(
    context: Context,
    token: String,
    category: String?,
    priority: String?,
    dateSort: String,
    onResult: (List<CommonTicket>) -> Unit
) {
    val list = mutableListOf<CommonTicket>()
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
        try {
            for (i in 0 until response.length()) {
                val o = response.getJSONObject(i)
                list.add(
                    CommonTicket(
                        id = o.optInt("id", 0),
                        title = o.optString("title", ""),
                        description = o.optString("description", ""),
                        category = o.optString("category", ""),
                        priority = o.optString("priority", ""),
                        status = o.optString("status", ""),
                        createdBy = o.optInt("created_by", 0),
                        assignedTo = o.optInt("assigned_to", 0),
                        createdAt = o.optString("created_at", ""),
                        isActive = o.optInt("is_active", 1),
                        accepted = o.optInt("accepted", o.optInt("acepted", 0))
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("AdminTicketList", "Parse error: ${e.message}")
        }

        if (response.length() == 0) {
            Log.w("API_RESPONSE", "Server returned 0 tickets for the current filter.")
        }

        onResult(list)
    }

    val error = Response.ErrorListener { err ->
        Log.e("AdminTicketList", err.message ?: "Volley error")
        onResult(emptyList())
    }

    val req = object : JsonArrayRequest(Request.Method.GET, url, null, listener, error) {
        override fun getHeaders(): MutableMap<String, String> =
            hashMapOf("Authorization" to "Bearer $token")
    }
    queue.add(req)
}
