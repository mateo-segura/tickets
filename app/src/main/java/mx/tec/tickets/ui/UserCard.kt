package mx.tec.tickets.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import mx.tec.tickets.model.User

@Composable
fun UserCard(user: User, navController: NavController) {
    val padding = 20.dp
    val fontSizeNormal = 11.sp
    val fontSizeTitle = 14.sp

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                val jsonUser = Gson().toJson(user)
                navController.navigate("detalle/${jsonUser}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
        )
    ) {
        Row {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    /*text = "${user.firstName} ${user.lastName}"*/
                    text = "Título",
                    fontSize = fontSizeTitle,
                    /*fontFamily = FontFamily.Cursive,*/
                    fontWeight = FontWeight.Bold
                )
                Text(
                    /*user.username*/
                    text = "Prioridad",
                    fontSize = fontSizeNormal,
                )
                Text(
                    /*user.email*/
                    text = "Técnico",
                    fontSize = fontSizeNormal,
                )
            }
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Descripción",
                        fontSize = fontSizeNormal
                    )
                    Spacer(Modifier.size(padding))
                    Text(
                        "Categoría",
                        fontSize = fontSizeNormal
                    )
                    Spacer(Modifier.size(padding))
                    Text(
                        "Fecha",
                        fontSize = fontSizeNormal,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    fontSize = fontSizeNormal,
                    style = LocalTextStyle.current.merge( /*Estilo del párrafo: https://developer.android.com/develop/ui/compose/text/style-paragraph*/
                        TextStyle(
                            lineHeight = 1.0.em,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                )
            }
        }
    }
}
