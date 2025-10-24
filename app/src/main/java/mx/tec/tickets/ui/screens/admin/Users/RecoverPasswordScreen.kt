package mx.tec.tickets.ui.screens.admin.Users

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current

    val emailArg = backStackEntry.arguments?.getString("email") ?: ""
    val tokenArg = backStackEntry.arguments?.getString("token") ?: ""   // ← token desde la ruta

    var email by remember { mutableStateOf(emailArg) }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val cardShape = RoundedCornerShape(24.dp)
    val headerGray = Color(0xFFDDDDDD)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            // Header centrado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerGray)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recuperar Contraseña",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center
                )
            }

            // Formulario
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email
                Text("Email", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    enabled = !isLoading,
                    leadingIcon = {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp,
                            color = Color(0xFFF0F0F0)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // New Password
                Text("NewPassword", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp,
                            color = Color(0xFFF0F0F0)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Confirm Password
                Text("ConfirmPassword", style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp,
                            color = Color(0xFFF0F0F0)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                // Botón principal: PATCH /usuarios/recuperar con headers
                Button(
                    onClick = {
                        val passOk = newPass.isNotBlank() && newPass.length >= 6
                        when {
                            tokenArg.isBlank() -> {
                                Toast.makeText(context, "Token ausente. Vuelve a iniciar sesión.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            !passOk -> {
                                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            newPass != confirmPass -> {
                                Toast.makeText(context, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            email.isBlank() -> {
                                Toast.makeText(context, "Falta el email.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                        }

                        isLoading = true

                        val url = "http://10.0.2.2:3000/usuarios/recuperar" // Emulador -> tu localhost
                        val body = JSONObject().apply {
                            put("email", email)
                            put("password", newPass)
                        }

                        val req = object : JsonObjectRequest(
                            Request.Method.PATCH,
                            url,
                            body,
                            { _ ->
                                isLoading = false
                                Toast.makeText(context, "Contraseña actualizada.", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            { err ->
                                isLoading = false
                                Toast.makeText(context, "Error: ${err.message ?: "no disponible"}", Toast.LENGTH_LONG).show()
                            }
                        ) {
                            override fun getHeaders(): MutableMap<String, String> {
                                // Ajusta el nombre del header si tu API usa 'x-token' o similar
                                return hashMapOf(
                                    "Content-Type" to "application/json",
                                    "Authorization" to "Bearer $tokenArg"
                                    // "x-token" to tokenArg
                                )
                            }
                        }

                        Volley.newRequestQueue(context).add(req)
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    } else {
                        Text("Recuperar", textAlign = TextAlign.Center)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botón secundario
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Cancelar", textAlign = TextAlign.Center)
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
