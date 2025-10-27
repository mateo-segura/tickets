package mx.tec.tickets.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.chat.Message
import org.json.JSONArray
import org.json.JSONObject

object ChatRepository {
    private const val BASE_URL = "http://10.0.2.2:3000/ticketsMessages"

    // GET mensajes por ticket
    fun fetchMessagesByTicket(
        context: Context,
        ticketId: Int,
        userId: Int,
        token: String,
        onResult: (List<Message>) -> Unit
    ) {
        val messages = mutableListOf<Message>()
        val queue = Volley.newRequestQueue(context)
        val url = "$BASE_URL/byTicket/$ticketId"

        Log.d("ChatAPI", "Fetching messages from: $url")

        val listener = Response.Listener<JSONArray> { response ->
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)

                val senderId = obj.getInt("sender_user_id")
                val isUserMessage = senderId == userId
                val fileId = obj.optString("file_id").takeIf { it != "null" && it.isNotBlank()}
                val authorName = if (isUserMessage) "Tú" else obj.optString("sender_name", "Agente")

                val message = Message(
                    author = authorName, // puedes adaptarlo
                    body = obj.getString("body"),
                    isUser = isUserMessage,
                    fileId = fileId
                )
                messages.add(message)
            }
            onResult(messages.reversed()) // opcional: para orden cronológico
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("ChatAPI", "Volley error: ${error.message}")
        }

        val request = object : JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        queue.add(request)
    }

    // POST enviar mensaje
// POST enviar mensaje
    fun sendMessage(
        context: Context,
        ticketId: Int,
        senderUserId: Int,
        body: String,
        token: String,
        fileId: Int? = null, // <--- 1. AÑADIMOS ESTE PARÁMETRO OPCIONAL
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val queue = Volley.newRequestQueue(context)
        val url = "$BASE_URL/insertar"

        val jsonBody = JSONObject()
        jsonBody.put("ticket_id", ticketId)
        jsonBody.put("sender_user_id", senderUserId)
        jsonBody.put("body", body)
        fileId?.let {
            jsonBody.put("file_id", it) // <--- 2. LO AÑADIMOS AL JSON SI EXISTE
        }

        val request = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener {
                onSuccess()
            },
            Response.ErrorListener { error ->
                Log.e("ChatAPI", "Error enviando mensaje: ${error.message}")
                onError(error.message ?: "Error desconocido")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        queue.add(request)
    }
}