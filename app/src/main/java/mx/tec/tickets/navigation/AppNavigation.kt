package mx.tec.tickets.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.tec.tickets.ui.screens.admin.AdminMenuScreen
import mx.tec.tickets.ui.screens.auth.MainScreen
import mx.tec.tickets.ui.screens.mesa.MesaMenuScreen
import mx.tec.tickets.ui.screens.mesa.components.MesaTicketDetail
import mx.tec.tickets.ui.screens.tecnico.MainNotificationScreen
import mx.tec.tickets.ui.screens.tecnico.TecnicoMenuScreen
import mx.tec.tickets.ui.screens.tecnico.components.NotificationDetail
import mx.tec.tickets.ui.screens.tecnico.components.NotificationList
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoAcceptedTicketDetail
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoNonAcceptedTicketDetail
import mx.tec.tickets.ui.screens.admin.MainAdminUserScreenNew
import mx.tec.tickets.ui.screens.admin.ModifyCategoryPriorityDialog
import mx.tec.tickets.ui.screens.admin.ReassignUserDialog
import mx.tec.tickets.ui.screens.admin.TechnicianItem
import mx.tec.tickets.ui.screens.admin.Users.ConfirmDeleteUserScreen
import mx.tec.tickets.ui.screens.admin.Users.RecoverPasswordScreen
import mx.tec.tickets.model.ApiClient
import mx.tec.tickets.model.TicketApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.tec.tickets.model.AssignTicketRequest
import mx.tec.tickets.model.PatchCategoryRequest
import mx.tec.tickets.model.PatchPriorityRequest
import mx.tec.tickets.model.UserApi


@Composable
fun AppNavigation() {
    val navController = rememberNavController() //router en react
    NavHost(
        navController = navController /*outlet en react */,
        startDestination = "mainscreen"/*raiz como en react, solo es la forma en como se llama*/
    ) {
        // rutas de autenticacion
        composable("mainscreen") {
            MainScreen(navController)
        }
        // rutas de tecnico
        composable(
            route = "tecnicomenuscreen?token={token}&role={role}&userid={userid}",
            arguments = listOf(
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                navArgument("role") { defaultValue = ""; type = NavType.StringType },
                navArgument("userid") { defaultValue = 0; type = NavType.IntType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0
            TecnicoMenuScreen(navController, token, role, userID)
        }
        composable(
            route = "detailnonaccepted/{nonacceptedticket}?token={token}",
            arguments = listOf(
                navArgument("nonacceptedticket") { type = NavType.StringType },
                navArgument("token") { defaultValue = ""; type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val nonacceptedticket = backStackEntry.arguments?.getString("nonacceptedticket")
            val token = backStackEntry.arguments?.getString("token") ?: ""
            TecnicoNonAcceptedTicketDetail(
                nonacceptedticket = nonacceptedticket ?: "",
                navController,
                token = token
            )
        }
        composable(
            // 1. Añade "userid" a la ruta
            route = "detailaccepted/{acceptedticket}?token={token}&userid={userid}",
            arguments = listOf(
                navArgument("acceptedticket") { type = NavType.StringType },
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                // 2. Añade el argumento "userid" a la lista
                navArgument("userid") { defaultValue = 0; type = NavType.IntType }
            ),
        ) { backStackEntry ->
            val acceptedticket = backStackEntry.arguments?.getString("acceptedticket")
            val token = backStackEntry.arguments?.getString("token") ?: ""
            // 3. Extrae el userID
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0

            TecnicoAcceptedTicketDetail(
                acceptedticket = acceptedticket ?: "",
                navController,
                userId = userID, // 4. Pasa el userId al composable
                token = token
            )
        }

        // Notificaciones
        composable("notificationList/{userId}") { backStack ->
            val userId = backStack.arguments?.getString("userId")?.toInt() ?: 0
            NotificationList(navController, userId)
        }

        composable("notificationDetail/{notification}") { backStack ->
            val json = backStack.arguments?.getString("notification") ?: ""
            NotificationDetail(json, navController)
        }

// pantalla principal de notificaciones
// pantalla principal de notificaciones
        composable(
            route = "mainnotificationscreen/{userid}",
            arguments = listOf(
                navArgument("userid") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0
            MainNotificationScreen(navController, userID)
        }


        // rutas de mesa
        composable(
            route = "mesamenuscreen?token={token}&role={role}&userid={userid}",
            arguments = listOf(
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                navArgument("role") { defaultValue = ""; type = NavType.StringType },
                navArgument("userid") { defaultValue = 0; type = NavType.IntType }

            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0
            MesaMenuScreen(navController, token, role, userID)
        }
        composable(
            route = "detailcommon/{acceptedticket}?token={token}&userid={userid}", // Añadido &userid
            arguments = listOf(
                navArgument("acceptedticket") { type = NavType.StringType },
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                navArgument("userid") { defaultValue = 0; type = NavType.IntType } // Añadido este argumento
            ),
        ) { backStackEntry ->
            val acceptedticket = backStackEntry.arguments?.getString("acceptedticket")
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0 // Añadida esta línea
            MesaTicketDetail(
                acceptedticket = acceptedticket ?: "",
                navController,
                userId = userID, // Pasamos el userId
                token = token
            )
        }
        // rutas de admin
        composable(
            route = "adminmenuscreen?token={token}&role={role}&userid={userid}",
            arguments = listOf(
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                navArgument("role") { defaultValue = ""; type = NavType.StringType },
                navArgument("userid") { defaultValue = 0; type = NavType.IntType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0
            AdminMenuScreen(navController, token, role, userID)
        }
        composable(
            route = "detailcommon/{acceptedticket}?token={token}&userid={userid}", // Añadido token y userid
            arguments = listOf(
                navArgument("acceptedticket") { type = NavType.StringType },
                navArgument("token") { defaultValue = ""; type = NavType.StringType }, // Añadido
                navArgument("userid") { defaultValue = 0; type = NavType.IntType } // Añadido
            ),
        ) { backStackEntry ->
            val acceptedticket = backStackEntry.arguments?.getString("acceptedticket")
            val token = backStackEntry.arguments?.getString("token") ?: "" // Añadido
            val userID = backStackEntry.arguments?.getInt("userid") ?: 0 // Añadido
            MesaTicketDetail(
                acceptedticket = acceptedticket ?: "",
                navController,
                userId = userID, // Pasamos el userId
                token = token    // Pasamos el token
            )
        }


        // CAMBIO: ruta nueva para la sección de Usuarios (solo añade, no modifica nada existente)
        composable(
            route = "adminusers?token={token}",
            arguments = listOf(
                navArgument("token") { defaultValue = ""; type = NavType.StringType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MainAdminUserScreenNew(navController = navController, token = token)
        }
        // FIN CAMBIO

        // ===== RUTAS NUEVAS: Usuarios -> Recuperar contraseña / Confirmar eliminación =====
        composable(
            route = "recoverPassword/{userId}?email={email}&token={token}",
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("email") { type = NavType.StringType; defaultValue = "" },
                navArgument("token") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            RecoverPasswordScreen(navController, backStackEntry)
        }

        composable(
            route = "confirmDelete/{userId}?email={email}&token={token}",
            arguments = listOf(
                navArgument("userId") { type = NavType.LongType },
                navArgument("email") { type = NavType.StringType; defaultValue = "" },
                navArgument("token") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            ConfirmDeleteUserScreen(navController, backStackEntry)
        }
// ===== FIN RUTAS NUEVAS =====

        // ===== RUTAS NUEVAS PARA LOS TRES PUNTITOS =====

        composable(
            route = "modifyCategoryPriority/{ticketId}",
            arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0

            // Token heredado del stack (ajusta si ya lo pasas por query)
            val token = remember {
                "Bearer " + (
                        backStackEntry.savedStateHandle.get<String>("token")
                            ?: navController.previousBackStackEntry?.arguments?.getString("token")
                            ?: ""
                        )
            }

            val service = remember { ApiClient.retrofit.create(TicketApi::class.java) }
            val scope = rememberCoroutineScope()

            var title by remember { mutableStateOf("Ticket #$ticketId") }
            var desc  by remember { mutableStateOf("") }
            var initCat by remember { mutableStateOf<String?>(null) }   // valor actual en DB
            var initPri by remember { mutableStateOf<String?>(null) }

            // Opciones visibles en UI (humanas), pero OJO: backend exige MAYÚSCULAS
            val categoriesUi = listOf("Hardware", "Software", "Redes", "Otro")
            val prioritiesUi = listOf("Baja", "Media", "Alta")

            // Fetch inicial
            LaunchedEffect(ticketId) {
                scope.launch(Dispatchers.IO) {
                    try {
                        val resp = service.getTicketById(ticketId, token).execute()
                        if (resp.isSuccessful) {
                            resp.body()?.let { t ->
                                withContext(Dispatchers.Main) {
                                    title = t.title
                                    desc  = t.description
                                    initCat = t.category        // llega ya en mayúsculas del backend
                                    initPri = t.priority
                                }
                            }
                        } else {
                            Log.e("ModifyCP", "GET failed: ${resp.code()} ${resp.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("ModifyCP", "GET exception: ${e.message}", e)
                    }
                }
            }

            // Helpers: mapear UI -> enum backend
            fun mapCategoryToEnum(ui: String): String = when (ui.trim().lowercase()) {
                "hardware" -> "HARDWARE"
                "software" -> "SOFTWARE"
                "red", "redes" -> "REDES"
                "otro" -> "OTRO"
                else -> ui.uppercase()
            }
            fun mapPriorityToEnum(ui: String): String = when (ui.trim().lowercase()) {
                "baja" -> "BAJA"
                "media" -> "MEDIA"
                "alta" -> "ALTA"
                else -> ui.uppercase()
            }

            ModifyCategoryPriorityDialog(
                ticketTitle = title,
                description = desc,
                categories = categoriesUi,
                priorities = prioritiesUi,
                selectedCategoryInitial = initCat?.replace('_',' ')?.lowercase()?.replaceFirstChar { it.titlecase() }, // opcional: maquilla a UI
                selectedPriorityInitial = initPri?.lowercase()?.replaceFirstChar { it.titlecase() },
                onConfirm = { newCatUi, newPriUi ->
                    scope.launch(Dispatchers.IO) {
                        try {
                            val newCat = mapCategoryToEnum(newCatUi)
                            val newPri = mapPriorityToEnum(newPriUi)

                            // Evita parches innecesarios
                            val needCat = newCat != (initCat ?: "")
                            val needPri = newPri != (initPri ?: "")

                            // Llama solo lo que cambió, y en orden independiente
                            if (needCat) {
                                val r1 = service.patchCategory(token,
                                    PatchCategoryRequest(ticketId, newCat)
                                ).execute()
                                if (!r1.isSuccessful) {
                                    Log.e("ModifyCP", "PATCH category failed: ${r1.code()} ${r1.message()}")
                                    // aquí podrías mostrar Snackbar/Toast si quieres
                                } else {
                                    initCat = newCat
                                }
                            }
                            if (needPri) {
                                val r2 = service.patchPriority(token,
                                    PatchPriorityRequest(ticketId, newPri)
                                ).execute()
                                if (!r2.isSuccessful) {
                                    Log.e("ModifyCP", "PATCH priority failed: ${r2.code()} ${r2.message()}")
                                } else {
                                    initPri = newPri
                                }
                            }

                            withContext(Dispatchers.Main) { navController.popBackStack() }
                        } catch (e: Exception) {
                            Log.e("ModifyCP", "PATCH exception: ${e.message}", e)
                        }
                    }
                },
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(
            route = "reassignUser/{ticketId}",
            arguments = listOf(
                navArgument("ticketId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0

            // Token heredado del stack (ajusta si ya lo pasas por query)
            val token = remember {
                "Bearer " + (
                        backStackEntry.savedStateHandle.get<String>("token")
                            ?: navController.previousBackStackEntry?.arguments?.getString("token")
                            ?: ""
                        )
            }

            val ticketService = remember { ApiClient.retrofit.create(TicketApi::class.java) }
            val userService   = remember { ApiClient.retrofit.create(UserApi::class.java) }
            val scope = rememberCoroutineScope()

            // Estado UI
            var title by remember { mutableStateOf("Ticket #$ticketId") }
            var desc  by remember { mutableStateOf("Selecciona técnico para reasignar") }
            var technicians by remember { mutableStateOf(listOf<TechnicianItem>()) }

            // 1) Cargar datos del ticket
            LaunchedEffect(ticketId) {
                scope.launch(Dispatchers.IO) {
                    try {
                        val resp = ticketService.getTicketById(ticketId, token).execute()
                        if (resp.isSuccessful) {
                            resp.body()?.let { t ->
                                withContext(Dispatchers.Main) {
                                    title = t.title.ifBlank { "Ticket #$ticketId" }
                                    desc  = t.description.ifBlank { "Selecciona técnico para reasignar" }
                                }
                            }
                        } else {
                            Log.e("Reassign", "GET ticket failed: ${resp.code()} ${resp.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("Reassign", "GET ticket exception: ${e.message}", e)
                    }
                }
            }

            // 2) Cargar lista de técnicos
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.IO) {
                    try {
                        val resp = userService.getTechnicians(token).execute()
                        if (resp.isSuccessful) {
                            val list = resp.body().orEmpty().map {
                                TechnicianItem(
                                    id = it.id,
                                    name = it.username,
                                    assignedCount = 0 // tu endpoint no devuelve conteo; lo dejamos en 0
                                )
                            }
                            withContext(Dispatchers.Main) { technicians = list }
                        } else {
                            Log.e("Reassign", "GET technicians failed: ${resp.code()} ${resp.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("Reassign", "GET technicians exception: ${e.message}", e)
                    }
                }
            }

            // 3) Mostrar diálogo y hacer PATCH al asignar
            ReassignUserDialog(
                ticketTitle = title,
                description = desc,
                technicians = technicians,
                onAssign = { userId ->
                    scope.launch(Dispatchers.IO) {
                        try {
                            val body = AssignTicketRequest(ticket_id = ticketId, user_id = userId)
                            val resp = userService.assignTicket(token, body).execute()
                            if (resp.isSuccessful) {
                                withContext(Dispatchers.Main) { navController.popBackStack() }
                            } else {
                                Log.e("Reassign", "PATCH assign failed: ${resp.code()} ${resp.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("Reassign", "PATCH assign exception: ${e.message}", e)
                        }
                    }
                },
                onDismiss = { navController.popBackStack() }
            )
        }
        // ===== FIN RUTAS NUEVAS =====



    }
}


