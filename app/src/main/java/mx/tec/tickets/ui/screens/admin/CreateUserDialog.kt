package mx.tec.tickets.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun CreateUserDialog(
    onDismiss: () -> Unit,
    // CAMBIO: ahora devuelve los datos capturados
    onSubmit: (email: String, username: String, password: String, role: UserRole) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {},
        text = {
            Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 0.dp) {
                var email by remember { mutableStateOf("") }
                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var role by remember { mutableStateOf(UserRole.ADMIN) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Crear nuevo usuario", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        label = { Text("Email") },
                        placeholder = { Text("correo@dominio.com") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                        )
                    )

                    OutlinedTextField(
                        value = username, onValueChange = { username = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Username") },
                        placeholder = { Text("admin@sistem.com") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = password, onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        label = { Text("Password") },
                        placeholder = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        )
                    )

                    OutlinedTextField(
                        value = confirm, onValueChange = { confirm = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        label = { Text("Confirm password") },
                        placeholder = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Rol", style = MaterialTheme.typography.titleMedium)
                        RoleOption("Administrador", UserRole.ADMIN, role) { role = it }
                        RoleOption("Mesa de Trabajo", UserRole.MESA, role) { role = it }
                        RoleOption("Técnico", UserRole.TECNICO, role) { role = it }
                    }

                    // CAMBIO: botón Aceptar ahora valida y envía datos al padre
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
                        Button(onClick = {
                            val valid = email.contains("@") &&
                                    username.isNotBlank() &&
                                    password.length >= 6 &&
                                    password == confirm
                            if (valid) {
                                onSubmit(email.trim(), username.trim(), password, role)
                                onDismiss()
                            }
                        }) { Text("Aceptar") }
                    }
                }
            }
        }
    )
}

@Composable
private fun RoleOption(
    text: String,
    value: UserRole,
    current: UserRole,
    onSelect: (UserRole) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(selected = current == value, onClick = { onSelect(value) })
        Text(text)
    }
}

enum class UserRole { ADMIN, MESA, TECNICO }
