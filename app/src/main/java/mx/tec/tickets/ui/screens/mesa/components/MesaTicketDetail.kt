package mx.tec.tickets.ui.screens.mesa.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import mx.tec.chat.ChatScreen
import mx.tec.tickets.model.CommonTicket
import mx.tec.tickets.model.NonAcceptedTicket
import mx.tec.tickets.ui.screens.SpinnerDropDown
import mx.tec.tickets.ui.theme.drawColoredShadow

@Composable
fun MesaTicketDetail(acceptedticket: String, navController: NavController) {
    val nonAcceptedTicketJson = Gson().fromJson(acceptedticket, CommonTicket::class.java)

    /*Importado de TicketScreen.kt (porque ya obteniamos la info del ticket en este componente y la navegación ya estaba implementada acá)*/
    val context = LocalContext.current
    var editBoxSize by remember { mutableStateOf(0.dp)}
    var infoColumnSize by remember { mutableStateOf(0.dp)}
    var initState by remember { mutableStateOf("Abierto") }
    val density = LocalDensity.current
    // var titulo by remember { mutableStateOf("Cargando...")}
    //val scrollState = rememberScrollState()

    Box(
        modifier = Modifier //el box hace que se sobrepongan
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        // Detalles anteriores
        //        Column(
        //            modifier = Modifier
        //                .padding(top = 56.dp)
        //                .padding(horizontal = 16.dp)
        //        ) {
        //            Text(ticketJson.title, style = MaterialTheme.typography.titleLarge)
        //            Spacer(Modifier.height(8.dp))
        //            Text(ticketJson.description)
        //        }

        /*Importado de TicketScreen.kt (porque ya obteniamos la info del ticket en este componente y la navegación ya estaba implementada acá)*/
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){

            Box (
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Surface (
                    modifier = Modifier
                        .padding(top = infoColumnSize + editBoxSize)
                ){
                    ChatScreen()
                }

                // Columna de datos de ticket
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = editBoxSize)
                        .drawColoredShadow(
                            color = Color.Black,           // Shadow color
                            alpha = 0.25f,                 // Opacity of the shadow
                            borderRadius = 8.dp,           // Match your box corner radius
                            shadowRadius = 12.dp,          // Blur radius of the shadow
                            offsetY = 4.dp,                // Vertical offset (shadow below the box)
                            offsetX = 0.dp                 // Horizontal offset (centered)
                        )
                        .onGloballyPositioned { coordinates ->
                            infoColumnSize = with(density) { coordinates.size.height.toDp() }
                        }
                        .background(Color.White)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        if (nonAcceptedTicketJson != null){
                            Text (
                                modifier = Modifier
                                    .padding(16.dp),
                                text = "Titulo: ${nonAcceptedTicketJson.title}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            Text (
                                modifier = Modifier
                                    .padding(16.dp),
                                text = "Titulo: Error, ticket nulo",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ){
                            if (nonAcceptedTicketJson != null){
                                Text (
                                    text = "Tecnico: ${nonAcceptedTicketJson.assignedTo}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            } else {
                                Text (
                                    text = "Tecnico: Error, ticket nulo",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Box(
                                    modifier = Modifier
                                        .background(Color.Red) // Cambiar color
                                        .size(20.dp)
                                )

                                if (nonAcceptedTicketJson != null){
                                    Text(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        text = "Prioridad - ${nonAcceptedTicketJson.priority}"
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        text = "Prioridad - Error, ticket nulo"
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Box(
                                    modifier = Modifier
                                        .background(Color.Green) // Cambiar el color
                                        .size(20.dp)
                                )

                                if (nonAcceptedTicketJson != null){
                                    Text(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        text = "Categoria - ${nonAcceptedTicketJson.category}"
                                    )
                                } else {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        text = "Categoria - Error, ticket nulo"
                                    )
                                }
                            }

                        }
                        SpinnerDropDown("Estado", initState) { initState = it }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            text = "Descripcion",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 4.dp, vertical = 16.dp)
                        ){
                            if (nonAcceptedTicketJson != null){
                                Text(
                                    text = nonAcceptedTicketJson.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            } else {
                                Text(
                                    text = "Error, ticket nulo",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                    }
                }

                // Caja de header superior "Edit"
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawColoredShadow(
                            color = Color.Black,           // Shadow color
                            alpha = 0.25f,                 // Opacity of the shadow
                            borderRadius = 8.dp,           // Match your box corner radius
                            shadowRadius = 12.dp,          // Blur radius of the shadow
                            offsetY = 4.dp,                // Vertical offset (shadow below the box)
                            offsetX = 0.dp                 // Horizontal offset (centered)
                        )
                        .background(Color.White)
                        .onGloballyPositioned { coordinates ->
                            editBoxSize = with(density) { coordinates.size.height.toDp() }
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text (
                        modifier = Modifier
                            .padding(16.dp),
                        /*.padding(WindowInsets.statusBars.asPaddingValues()), causaba problemas con el scaffold de TecnicoMenuScreen.kt (falta arreglar) */
                        text = "Edit",
                        style = MaterialTheme.typography.titleLarge.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(2f, 2f), // x, y displacement in pixels
                                blurRadius = 4f          // how soft the shadow is
                            )
                        )
                    )
                }
            }
        }

        // Cambiar posicion para que se sobreponga entre los demás componentes
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

    }

}