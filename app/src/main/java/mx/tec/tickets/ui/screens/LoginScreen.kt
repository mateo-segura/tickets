package mx.tec.tickets.ui.screens

import android.R.attr.label
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue


@Composable
fun LoginScreen(onLoginSuccess:()->Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    Column {
        OutlinedTextField(
            value=usuario,
            onValueChange = {usuario=it},    //it cambia el valor del cuadro
            label={ Text("Usuario") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password=it},
            label={ Text("Contraseña") }
        )
        Button(
            //user and password valid
            onClick = {onLoginSuccess()} //interfaz: pasar del hijo al padre
        ){
            Text("Iniciar")
        }
    }
}