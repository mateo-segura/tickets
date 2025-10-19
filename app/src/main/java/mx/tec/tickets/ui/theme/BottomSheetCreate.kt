package mx.tec.tickets.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.tickets.ui.screens.SpinnerDropDown


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCreate(
    showSheetCreate: Boolean,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // optional, prevents half-expanded state
    )

    // Bottom sheet itself
    if (showSheetCreate) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            var titleText by remember { mutableStateOf("") }
            var descriptionText by remember { mutableStateOf("") }
            var initState by remember { mutableStateOf("Abierto") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){

                    Row (
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .weight(1f)
                            //.height(60.dp)
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(text = "Titulo:",
                            style = MaterialTheme.typography.titleSmall)

                        TextField(
                            value = titleText,
                            onValueChange = { titleText = it },
                            label = { Text(text = "Titulo",
                                style = MaterialTheme.typography.bodySmall) },
                            //modifier = Modifier.height(60.dp)
                        )
                    }
                    Box(
                        modifier = Modifier.weight(1f)
                    ){
                        SpinnerDropDown("Estado", initState) { initState = it }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ){
                    Column{
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            text = "Descripcion:",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 4.dp, vertical = 16.dp)
                                .border(width = 1.dp, color = Color.Black),
                            value = descriptionText,
                            onValueChange = { descriptionText = it },
                            label = { Text("Escribe la descripción") }
                        )
                    }

                }




                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )) {
                        Text("Crear")
                    }

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )) {
                        Text("Cancelar")
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ShowBottomSheetCreate() {

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