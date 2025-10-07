package mx.tec.tickets.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.model.User
import org.json.JSONArray

@Composable
fun UserList(navController: NavController) {
    val context = LocalContext.current
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        fetchUsers(context) { fetchedUsers ->
            users = fetchedUsers
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.statusBarsPadding()
            // Status bars padding estaba causando padding conflictivo
    ) {
        items(users) { user ->
            UserCard(user, navController)
        }
    }
}

//Consumir el API, Recibe: ?, Devuelve: List<User>
fun fetchUsers(context: Context, onResult: (List<User>) -> Unit) {
    val users = mutableListOf<User>()
    val queue = Volley.newRequestQueue(context)

    val url = "https://jsonplaceholder.typicode.com/users"
    val metodo = Request.Method.GET
    val listener = Response.Listener<JSONArray> { response ->
        for (i in 0 until response.length()) {
            val user = User(
                response.getJSONObject(i).getString("username"),
                response.getJSONObject(i).getString("email"),
                response.getJSONObject(i).getString("name"),
                ""
            )
            users.add(user)
        }
        onResult(users)
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