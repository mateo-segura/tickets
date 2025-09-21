package mx.tec.tickets.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun MainScreen() {
    var isLogged by remember {mutableStateOf(false)}
    if(isLogged)
        MenuScreen()
    else
        LoginScreen() {
            isLogged=true
        }
}