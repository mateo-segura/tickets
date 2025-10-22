package mx.tec.tickets.ui.screens.admin


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mx.tec.tickets.ui.screens.admin.adminUsersScreen.AdminUserListNew

@Composable
fun MainAdminUserScreenNew(navController: NavController, token: String) {
    AdminUserListNew(token = token, navController = navController)
}
