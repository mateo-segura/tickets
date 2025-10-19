package mx.tec.tickets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.tec.tickets.ui.screens.MainScreen
import mx.tec.tickets.ui.screens.MesaMenuScreen
import mx.tec.tickets.ui.screens.TecnicoMenuScreen
import mx.tec.tickets.ui.screens.TicketDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() //router en react

    NavHost(
        navController = navController /*outlet en react */,
        startDestination = "mainscreen"/*raiz como en react, solo es la forma en como se llama*/
    ) {
        //declarar rutas
        composable("mainscreen"){
            MainScreen(navController)
        }
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
            TecnicoMenuScreen(navController, token, role,userID)
        }
        composable(
            route = "mesamenuscreen?token={token}&role={role}",
            arguments = listOf(
                navArgument("token") { defaultValue = ""; type = NavType.StringType },
                navArgument("role") { defaultValue = ""; type = NavType.StringType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: ""
            MesaMenuScreen(navController, token, role)
        }
//        composable("lista" /*id*/) {
//            TecnicoTicketList(navController /*cada componente necesita navcontroller*/)
//        }
        composable(
            route = "detalle/{ticket}",  //no se pueden mandar a objetos, se necesita mandar en formato json
            arguments = listOf(navArgument("ticket") { type = NavType.StringType }),
        ) { backStackEntry ->
            val ticket = backStackEntry.arguments?.getString("ticket")
            TicketDetailScreen(ticket = ticket ?: "", navController)
        }
    }
}