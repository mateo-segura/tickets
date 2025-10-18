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
    //cambiar para recibir el objeto LoginResponse
    var jwtToken by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    if(isLogged){
        //un switch con cada rol
        if(role=="TECNICO")
            navController.navigate("tecnicomenuscreen?token=$jwtToken&role=$role")
        if(role=="MESA")
            navController.navigate("mesamenuscreen?token=$jwtToken&role=$role")
    }
    else{
        LoginScreen { token, userRole ->
            jwtToken = token
            role = userRole
            isLogged = true
        }
    }
}