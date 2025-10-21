package mx.tec.tickets.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    var userID by remember { mutableIntStateOf(0) }

    if(isLogged){
        //un switch con cada rol
        if(role=="TECNICO")
            navController.navigate("tecnicomenuscreen?token=$jwtToken&role=$role&userid=$userID")
        if(role=="MESA")
            navController.navigate("mesamenuscreen?token=$jwtToken&role=$role&userid=$userID")
        if(role=="ADMIN")
            navController.navigate("adminmenuscreen?token=$jwtToken&role=$role&userid=$userID")
    }
    else{
        LoginScreen { token, userRole, userid ->
            jwtToken = token
            role = userRole
            userID = userid
            isLogged = true
        }
    }
}