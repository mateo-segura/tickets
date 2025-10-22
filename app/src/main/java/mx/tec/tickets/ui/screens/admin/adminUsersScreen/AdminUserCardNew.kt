package mx.tec.tickets.ui.screens.admin.adminUsersScreen

import android.net.Uri
import mx.tec.tickets.ui.screens.admin.Users.AppUser
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AdminUserCardNew(
    user: AppUser,
    navController: NavController,
    token: String,
    modifier: Modifier = Modifier
) {
    var menuOpen by remember { mutableStateOf(false) }
    val emailEncoded = Uri.encode(user.email)
    val tokenEncoded = Uri.encode(token)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del usuario
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.displayName ?: "Nombre",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Rol: ${user.role ?: "—"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón de tres puntitos + menú
            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Opciones")
                }

                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false },
                    offset = DpOffset(0.dp, 0.dp),
                    properties = PopupProperties(focusable = true)
                ) {
                    // --- Recuperar Contraseña ---
                    DropdownMenuItem(
                        text = { Text("Recuperar contraseña") },
                        leadingIcon = { Icon(Icons.Outlined.Refresh, contentDescription = null) },
                        onClick = {
                            menuOpen = false
                            val emailEncoded = URLEncoder.encode(
                                user.email ?: "",
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate(
                                "recoverPassword/${user.id}?email=$emailEncoded&token=$tokenEncoded"
                            )
                        }
                    )

                    // --- Eliminar Usuario ---
                    DropdownMenuItem(
                        text = { Text("Eliminar usuario") },
                        leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                        onClick = {
                            menuOpen = false
                            val emailEncoded = URLEncoder.encode(
                                user.email ?: "",
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate(
                                "confirmDelete/${user.id}?email=$emailEncoded&token=$tokenEncoded"
                            )
                        }
                    )
                }
            }
        }
        Divider()
    }
}
