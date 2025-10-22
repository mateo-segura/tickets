package mx.tec.tickets.ui.screens.admin.adminUsersScreen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import mx.tec.tickets.ui.screens.admin.Users.AppUser
import mx.tec.tickets.ui.screens.admin.Users.DataUsers.UsersRepository
import mx.tec.tickets.ui.screens.admin.CreateUserDialog
import mx.tec.tickets.ui.screens.admin.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserListNew(token: String, navController: NavController ) {
    val context: Context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()   // 👈 para lanzar snackbars
    var users by remember { mutableStateOf<List<AppUser>>(emptyList()) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Carga inicial de usuarios
    LaunchedEffect(Unit) {
        UsersRepository.fetchUsers(
            context = context,
            token = token,
            onSuccess = { users = it },
            onError = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error al cargar usuarios")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Usuarios", style = MaterialTheme.typography.titleLarge) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Crear usuario")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(users, key = { it.id }) { user ->
                AdminUserCardNew(user = user, navController = navController, token = token)
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    // Diálogo para crear usuario
    if (showCreateDialog) {
        CreateUserDialog(
            onDismiss = { showCreateDialog = false },
            onSubmit = { email, username, password, role ->
                val roleString = when (role) {
                    UserRole.ADMIN -> "ADMIN"
                    UserRole.MESA -> "MESA"
                    UserRole.TECNICO -> "TECNICO"
                }

                UsersRepository.createUser(
                    context = context,
                    token = token,
                    email = email,
                    username = username,
                    password = password,
                    role = roleString,
                    onSuccess = { created ->
                        // refrescar lista tras crear
                        UsersRepository.fetchUsers(
                            context = context,
                            token = token,
                            onSuccess = { users = it },
                            onError = { }
                        )
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Usuario '${created.username ?: created.email}' creado correctamente"
                            )
                        }
                    },
                    onError = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Error al crear el usuario")
                        }
                    }
                )
            }
        )
    }
}
