package mx.tec.tickets.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.tickets.R
import org.json.JSONObject

@Composable
fun LoginScreen(onLoginSuccess: (jwtToken: String, role: String, userid: Int) -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // ==============================
        // FONDOS DECORATIVOS
        // ==============================
        Image(
            painter = painterResource(id = R.drawable.cuadrado),
            contentDescription = "Decorativo izquierdo",
            modifier = Modifier
                .size(450.dp)
                .graphicsLayer {
                    clip = false
                    rotationZ = 45f
                    translationX = -550f
                    translationY = -450f
                }
                .zIndex(0f)
        )

        Image(
            painter = painterResource(id = R.drawable.cuadrado),
            contentDescription = "Decorativo derecho",
            modifier = Modifier
                .size(400.dp)
                .graphicsLayer {
                    clip = false
                    rotationZ = 45f
                    translationX = 550f
                    translationY = 800f
                }
                .zIndex(0f)
        )

        // ==============================
        // CONTENIDO DEL LOGIN
        // ==============================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .zIndex(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LOGO Y TÍTULOS
            Image(
                painter = painterResource(id = R.drawable.maclogo),
                contentDescription = "Logo de MAC Computadoras",
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "MAC Service Desk",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CAMPOS DE ENTRADA (lógica intacta)
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Contraseña") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE LOGIN (sin cambios funcionales)
            Button(
                onClick = {
                    val jsonObject = JSONObject().apply {
                        put("email", usuario)
                        put("password", password)
                    }
                    handleLogin(context, jsonObject) { jwtToken, role, userid ->
                        onLoginSuccess(jwtToken, role, userid)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Iniciar Sesión")
            }
        }
    }
}

// ===================================================
// FUNCIÓN DE LOGIN (NO SE TOCA NADA DE LA LÓGICA)
// ===================================================
fun handleLogin(
    context: Context,
    jsonObject: JSONObject,
    onSuccess: (jwtToken: String, role: String, userid: Int) -> Unit
) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://Api-tickets-env.eba-3z343hb2.us-east-1.elasticbeanstalk.com/login"
    val metodo = Request.Method.POST

    val listener = Response.Listener<JSONObject> { response ->
        println(response)
        try {
            val jwtToken = response.getString("token")
            val role = response.getString("role")
            val userid = response.getInt("id")
            onSuccess(jwtToken, role, userid)
        } catch (e: Exception) {
            Log.e("Login", "Error al parsear respuesta", e)
        }
    }

    val errorListener = Response.ErrorListener { error ->
        Log.e("VolleyError", error.message.toString())
    }

    val request = JsonObjectRequest(metodo, url, jsonObject, listener, errorListener)
    queue.add(request)
}
