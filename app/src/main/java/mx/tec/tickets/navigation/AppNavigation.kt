package mx.tec.tickets.navigation

import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.tec.tickets.ui.TicketList
import mx.tec.tickets.ui.screens.TicketDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() //router en react

    NavHost(
        navController = navController /*outlet en react */,
        startDestination = "lista"/*raiz como en react, solo es la forma en como se llama*/
    ) {
        //declarar rutas
        composable("lista" /*id*/) {
            TicketList(navController /*cada componente necesita navcontroller*/)
        }
        composable(
            route = "detalle/{ticket}",  //no se pueden mandar a objetos, se necesita mandar en formato json
            arguments = listOf(navArgument("ticket") { type = NavType.StringType }),
            //enterTransition = { slideInHorizontally(initialOffsetX = { it }) }, // desde la derecha, desde otra pantalla
            //exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },   // hacia la izquierda
            //enterTransition = { fadeIn(animationSpec = tween(500)) },
            //exitTransition = { fadeOut(animationSpec = tween(500)) },
            enterTransition = { expandIn(expandFrom = Alignment.Center) },
            exitTransition = { shrinkOut(shrinkTowards = Alignment.Center) },
            //popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) }, // desde popBackStack
            //popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) { backStackEntry ->
            val ticket = backStackEntry.arguments?.getString("ticket")
            TicketDetailScreen(ticket = ticket ?: "", navController)
        }
    }
}