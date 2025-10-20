package mx.tec.tickets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.tec.tickets.ui.screens.auth.MainScreen
import mx.tec.tickets.ui.screens.mesa.MesaMenuScreen
import mx.tec.tickets.ui.screens.mesa.components.MesaTicketDetail
import mx.tec.tickets.ui.screens.tecnico.TecnicoMenuScreen
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoAcceptedTicketDetail
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoNonAcceptedTicketDetail

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
            route = "detailnonaccepted/{nonacceptedticket}",  //no se pueden mandar a objetos, se necesita mandar en formato json
            arguments = listOf(navArgument("nonacceptedticket") { type = NavType.StringType }),
        ) { backStackEntry ->
            val nonacceptedticket = backStackEntry.arguments?.getString("nonacceptedticket")
            TecnicoNonAcceptedTicketDetail(
                nonacceptedticket = nonacceptedticket ?: "",
                navController
            )
        }
        composable(
            route = "detailaccepted/{acceptedticket}",  //no se pueden mandar a objetos, se necesita mandar en formato json
            arguments = listOf(navArgument("acceptedticket") { type = NavType.StringType }),
        ) { backStackEntry ->
            val acceptedticket = backStackEntry.arguments?.getString("acceptedticket")
            TecnicoAcceptedTicketDetail(acceptedticket = acceptedticket ?: "", navController)
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
            route = "detailcommon/{acceptedticket}",  //no se pueden mandar a objetos, se necesita mandar en formato json
            arguments = listOf(navArgument("acceptedticket") { type = NavType.StringType }),
        ) { backStackEntry ->
            val acceptedticket = backStackEntry.arguments?.getString("acceptedticket")
            MesaTicketDetail(acceptedticket = acceptedticket ?: "", navController)
        }
    }
}