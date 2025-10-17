package mx.tec.tickets.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController


@Composable
fun MainScreen(navController: NavController) {
    var isLogged by remember {mutableStateOf(false)}
    if(isLogged)
        navController.navigate("menuscreen")
    else
        LoginScreen() {
            isLogged=true
        }
}