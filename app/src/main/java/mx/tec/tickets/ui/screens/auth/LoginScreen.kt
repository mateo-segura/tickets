package mx.tec.tickets.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


@Composable
fun LoginScreen(onLoginSuccess: (jwtToken: String, role: String,userid:Int) -> Unit) { /*/interfaz: pasar del hijo al padre, { isLogged = true } se pasa desde el padre como onLoginSuccess()*/
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },    //it cambia el valor del cuadro
            label = { Text("Usuario") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Contraseña") }
        )
        Button(
            onClick = {
                val jsonObject = JSONObject()
                jsonObject.put("email", usuario)
                jsonObject.put("password", password)
                //SI no hubo error: onLoginSuccess() (pasar role y jwt)
                handleLogin(context,jsonObject){jwtToken, role,userid ->
                    onLoginSuccess(jwtToken, role,userid)
                }
                // si hubo error mostrar error
            }
        ) {
            Text("Entrar")
        }
    }
}
fun handleLogin(context: Context,jsonObject: JSONObject,    onSuccess: (jwtToken: String, role: String,userid:Int) -> Unit){
    //https://escapesequence89.medium.com/volley-http-for-android-60999099ddda
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:3000/login"
    val metodo = Request.Method.POST
    val listener=Response.Listener<JSONObject>{ response ->
        println(response)
        try{
            val jwtToken = response.getString("token")
            val role = response.getString("role");
            val userid=response.getInt("id")
            onSuccess(jwtToken, role,userid)
        } catch (e:Exception){
            Log.e("Login", "Error al parsear respuesta", e)
        }
    }
    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message.toString())
    }
    val request=JsonObjectRequest(metodo,url,jsonObject, listener,errorListener)
    queue.add(request)
}
