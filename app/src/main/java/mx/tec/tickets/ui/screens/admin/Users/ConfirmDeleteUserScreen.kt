package mx.tec.tickets.ui.screens.admin.users

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@Composable
fun ConfirmDeleteUserScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry
) {
    // ------------------------------
    // Lectura de argumentos y fallback a SharedPreferences
    // ------------------------------
    val emailArg = backStackEntry.arguments?.getString("email")
        ?: backStackEntry.arguments?.getString("name")
        ?: ""

    // 1) intentar obtener desde la ruta
    val tokenFromArgs = backStackEntry.arguments?.getString("token") ?: ""

    // 2) fallback: si la ruta vino sin token, intentamos leer el guardado en login
    val prefs = navController.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = tokenFromArgs.ifBlank { prefs.getString("token", "") ?: "" }

    var isLoading by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "¿Estás seguro de hacer esto?",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "¿Seguro que quieres eliminar al usuario con email:\n$emailArg?\nEsta acción no se puede deshacer.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (emailArg.isBlank()) {
                                        Toast.makeText(
                                            navController.context,
                                            "No se recibió el email del usuario.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                    if (token.isBlank()) {
                                        // Con el fallback, solo llegarás aquí si no hay token en ningún lado
                                        Toast.makeText(
                                            navController.context,
                                            "Token ausente. Vuelve a iniciar sesión.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    isLoading = true

                                    // Emulador Android: 10.0.2.2 apunta a tu localhost
                                    // OJO: revisa que el endpoint coincida con tu backend: "desabilitar" vs "deshabilitar"
                                    val url = "http://10.0.2.2:3000/usuarios/desabilitar"

                                    val body = JSONObject().apply {
                                        put("email", emailArg)
                                    }

                                    val req = object : JsonObjectRequest(
                                        Request.Method.PATCH,
                                        url,
                                        body,
                                        { _ ->
                                            isLoading = false
                                            Toast.makeText(
                                                navController.context,
                                                "Usuario deshabilitado correctamente.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        },
                                        { err ->
                                            isLoading = false
                                            val code = err.networkResponse?.statusCode
                                            val msg = when (code) {
                                                400 -> "Solicitud inválida. Verifica los datos."
                                                401 -> "Sesión expirada o token inválido. Inicia sesión de nuevo."
                                                403 -> "No tienes permisos para esta acción."
                                                404 -> "Usuario no encontrado."
                                                409 -> "Conflicto al deshabilitar. Revisa reglas del backend."
                                                500 -> "Error del servidor. Inténtalo más tarde."
                                                else -> "Error al eliminar: ${err.message ?: "desconocido"}"
                                            }
                                            Toast.makeText(navController.context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    ) {
                                        override fun getHeaders(): MutableMap<String, String> {
                                            // Si tu API usa 'x-token', cambia la clave por "x-token" to token
                                            return hashMapOf(
                                                "Content-Type" to "application/json",
                                                "Authorization" to "Bearer $token"
                                            )
                                        }
                                    }

                                    Volley.newRequestQueue(navController.context).add(req)
                                },
                                enabled = !isLoading,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text("Aceptar")
                                }
                            }

                            OutlinedButton(
                                onClick = { navController.popBackStack() },
                                enabled = !isLoading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}
