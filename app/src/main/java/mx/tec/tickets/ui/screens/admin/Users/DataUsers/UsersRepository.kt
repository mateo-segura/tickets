package mx.tec.tickets.ui.screens.admin.Users.DataUsers

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.ui.screens.admin.Users.AppUser
import org.json.JSONArray
import org.json.JSONObject

object UsersRepository {

    // Emulador → host local
    private const val BASE_URL = "http://Api-tickets-env.eba-3z343hb2.us-east-1.elasticbeanstalk.com"
    private const val USERS_ENDPOINT = "$BASE_URL/usuarios"
    private const val USERS_INSERT_ENDPOINT = "$BASE_URL/usuarios/insertar"  // ← CAMBIO: endpoint correcto para crear

    /** GET /usuarios */
    fun fetchUsers(
        context: Context,
        token: String,
        onSuccess: (List<AppUser>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val queue = Volley.newRequestQueue(context)

        val req = object : JsonArrayRequest(
            Request.Method.GET, USERS_ENDPOINT, null,
            { arr: JSONArray ->
                val list = mutableListOf<AppUser>()
                for (i in 0 until arr.length()) {
                    val o: JSONObject = arr.getJSONObject(i)
                    list.add(
                        AppUser(
                            id = o.optInt("id"),
                            email = o.optString("email"),
                            username = o.optString("username", null),
                            role = o.optString("role")
                        )
                    )
                }
                onSuccess(list)
            },
            { err -> onError(err) }
        ) {
            override fun getHeaders(): MutableMap<String, String> = buildHeaders(token)
        }

        queue.add(req)
    }

    /** POST /usuarios/insertar */
    fun createUser(
        context: Context,
        token: String,
        email: String,
        username: String,            // opcional para el backend; lo ignorará si no lo usa
        password: String,
        role: String,                // "ADMIN" | "MESA" | "TECNICO"
        onSuccess: (AppUser) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val queue = Volley.newRequestQueue(context)

        // La doc SOLO exige email, password, role. Mandamos eso.
        val body = JSONObject().apply {
            put("username", username)   // ← OBLIGATORIO SEGÚN EL LOG
            put("email", email)
            put("password", password)
            put("role", role)           // "ADMIN" | "MESA" | "TECNICO" en mayúsculas
        }

        val req = object : JsonObjectRequest(
            Request.Method.POST, USERS_INSERT_ENDPOINT, body,
            { obj ->
                // Si el backend no retorna el usuario, igual haremos re-fetch en la UI
                val created = AppUser(
                    id = obj.optInt("id", -1),
                    email = obj.optString("email", email),
                    username = obj.optString("username", username),
                    role = obj.optString("role", role)
                )
                onSuccess(created)
            },
            { err ->
                val code = err.networkResponse?.statusCode
                val payload = err.networkResponse?.data?.let { String(it, Charsets.UTF_8) }
                android.util.Log.e("UsersRepository", "POST /usuarios/insertar [$code]: $payload", err)
                onError(RuntimeException("HTTP $code: $payload"))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> = buildHeaders(token, json = true)
        }

        queue.add(req)
    }

    // Helpers
    private fun buildHeaders(token: String, json: Boolean = false): MutableMap<String, String> {
        val h = HashMap<String, String>()
        // Si tu endpoint NO requiere token, puedes quitar esta línea sin drama.
        h["Authorization"] = "Bearer $token"
        h["Accept"] = "application/json"
        if (json) h["Content-Type"] = "application/json"
        return h
    }
}
