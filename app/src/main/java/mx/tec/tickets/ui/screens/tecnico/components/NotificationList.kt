package mx.tec.tickets.ui.screens.tecnico.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.model.NotificationItem
import org.json.JSONArray

@Composable
fun NotificationList(
    navController: NavController,
    userID: Int
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var notifications by remember { mutableStateOf(listOf<NotificationItem>()) }

    LaunchedEffect(Unit) {
        fetchNotifications(context, userID) { fetched ->
            notifications = fetched
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(notifications, key = { it.id }) { notification ->
            NotificationCard(notification, navController)
        }
    }
}

fun fetchNotifications(
    context: Context,
    userID: Int,
    onResult: (List<NotificationItem>) -> Unit
) {
    val list = mutableListOf<NotificationItem>()
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/notificaciones/$userID"

    val listener = Response.Listener<JSONArray> { response ->
        for (i in 0 until response.length()) {
            val obj = response.optJSONObject(i) ?: continue
            val dataObj = obj.optJSONObject("data")

            val item = NotificationItem(
                id = obj.optInt("id", 0),
                user_id = obj.optInt("user_id", 0),
                type = obj.optString("type", ""),
                data = NotificationItem.NotificationData(
                    message = dataObj?.optString("message", "Sin mensaje") ?: "Sin mensaje",
                    ticket_id = dataObj?.optInt("ticket_id", 0) ?: 0,
                    message_id = dataObj?.optInt("message_id", 0) ?: 0,
                    sender_user_id = dataObj?.optInt("sender_user_id", 0) ?: 0
                ),
                isRead = obj.optInt("is_read", 0),
                created_at = obj.optString("created_at", "")
            )

            list.add(item)
        }
        onResult(list)
    }

    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message ?: "Error desconocido")
    }

    val request = JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener)
    queue.add(request)
}
