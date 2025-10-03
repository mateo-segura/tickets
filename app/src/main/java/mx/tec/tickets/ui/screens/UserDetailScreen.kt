package mx.tec.tickets.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import mx.tec.tickets.model.User

@Composable
fun UserDetailScreen(user: String, navController: NavController) {
    val userJson = Gson().fromJson(user, User::class.java)
    Box(modifier = Modifier //el box hace que se sobrepongan
        .statusBarsPadding()
        .fillMaxSize()) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }
        Column(modifier = Modifier
            .padding(top = 56.dp)
            .padding(horizontal = 16.dp)) {
            Text("Detalles del usuario", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Name: ${userJson.firstName}")
            Text("Username: ${userJson.username}")
            Text("Enamil: ${userJson.email}")
        }
    }

}