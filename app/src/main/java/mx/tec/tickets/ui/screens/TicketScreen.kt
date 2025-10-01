package mx.tec.tickets.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.chat.ChatScreen
import mx.tec.tickets.ui.theme.drawColoredShadow

// Vista de interior del ticket

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VistaInteriorTicket() {
    var editBoxSize by remember { mutableStateOf(0.dp)}
    var infoColumnSize by remember { mutableStateOf(0.dp)}
    var initState by remember { mutableStateOf("Abierto") }
    val density = LocalDensity.current
    //val scrollState = rememberScrollState()

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
                    Text (
                        modifier = Modifier
                            .padding(16.dp),
                        text = "Titulo: ", // Agregar titulo del ticket
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ){
                        Text (
                            text = "Tecnico: ", // Agregar tecnico asignado
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box(
                                modifier = Modifier
                                    .background(Color.Red) // Cambiar color
                                    .size(20.dp)
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Prioridad - "
                            )
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
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Categoria - "
                            )
                        }

                    }
                    SpinnerDropDown("Estado", initState) { initState = it }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ){
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
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
                        Text(
                            text = "Lorem ipsum dolor carlos no qwuiere jugar nightreign.",
                            style = MaterialTheme.typography.bodyMedium
                        )
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
                        .padding(16.dp)
                        .padding(WindowInsets.statusBars.asPaddingValues()),
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

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerDropDown(
    texto: String,
    seleccion: String,
    onSeleccion: (String) -> Unit
){
    val estado = listOf("Abierto", "En proceso", "Cerrado")
    var abierto by remember { mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = abierto,
        onExpandedChange = { abierto = !abierto }
    ) {
        TextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(texto)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(abierto) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .width(140.dp)
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            estado.forEach{ estado ->
                DropdownMenuItem(
                    text = { Text(estado) },
                    onClick = {
                        onSeleccion(estado)
                        abierto = false
                    }
                )
            }
        }
    }

}