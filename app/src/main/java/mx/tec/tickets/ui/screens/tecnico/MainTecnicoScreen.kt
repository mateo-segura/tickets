package mx.tec.tickets.ui.screens.tecnico

import android.R.attr.enabled
import android.R.attr.type
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoAcceptedTicketList
import mx.tec.tickets.ui.screens.tecnico.components.TecnicoNonAcceptedTicketList
import mx.tec.tickets.ui.theme.drawColoredShadow
import kotlin.math.exp

// Vista principal tecnico

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainTecnicoScreen(navController: NavController,token: String,role: String,userID: Int) {
    var navBarSize by remember { mutableStateOf(0.dp)}
    var notifIcon by remember { mutableStateOf(0.dp)}
    var showSheet by remember { mutableStateOf(false) }
    var showSheetNew by remember { mutableStateOf(false) }
    var showSheetCreate by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    var selectedCategory by remember {mutableStateOf<String?>(null)}
    var selectedPriority by remember {mutableStateOf<String?>(null)}
    var selectedDateSort by remember {mutableStateOf<String>("DESC")}

    // 1. Create the Refresh State (key)
    var acceptedRefreshKey by remember { mutableIntStateOf(0) }

    // 2. Define the action to trigger a refresh
    val triggerAcceptedRefresh: () -> Unit = {
        acceptedRefreshKey++
    }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Header "Mis Tickets"
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
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text (
                modifier = Modifier
                    .padding(16.dp),
                    /*.padding(WindowInsets.statusBars.asPaddingValues()), causaba problemas con el scaffold de TecnicoMenuScreen.kt https://drive.google.com/file/d/1IXaWoe8pqS4JKnhNISbMsP_zsDsrOBsa/view?usp=sharing (falta arreglar) */
                text = "Mis Tickets: ${role}",
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f), // x, y displacement in pixels
                        blurRadius = 4f          // how soft the shadow is
                    )
                )
            )
        }

        // Column para "Mis Tickets" y botones
        Column (
            modifier = Modifier.weight(1f)
        ){
            // Botones de filtros
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){

                // Filtro de Categoria
                FilterDropDown(
                    label = "Categoria",
                    options = listOf("HARDWARE", "SOFTWARE", "NETWORK", "TODOS"),
                    currentValue = selectedCategory,
                    onValueSelected = { newValue ->
                        selectedCategory = if (newValue == "TODOS") null else newValue
                        triggerAcceptedRefresh()
                    }
                )

                // Filtro de Prioridad
                FilterDropDown(
                    label = "Prioridad",
                    options = listOf("ALTA", "MEDIA", "BAJA", "TODOS"),
                    currentValue = selectedPriority,
                    onValueSelected = { newValue ->
                        selectedPriority = if (newValue == "TODOS") null else newValue
                        triggerAcceptedRefresh()
                    }
                )

                // Filtro de fecha
                FilterDropDown(
                    label = "Fecha",
                    options = listOf("Mas nuevo", "Mas antiguo"),
                    currentValue = if (selectedDateSort == "DESC") "Mas nuevo" else "Mas antiguo",
                    onValueSelected = { newValue ->
                        selectedDateSort = if (newValue == "Mas nuevo") "DESC" else "ASC"
                        triggerAcceptedRefresh()
                    }
                )

            }

            // Espacio de tickets Mis Tickets (Aceptados)
            Column () {
                TecnicoAcceptedTicketList(
                    navController,
                    userID,
                    token,
                    acceptedRefreshKey,
                    selectedCategory,
                    selectedPriority,
                    selectedDateSort
                )
            }

        }

        // Header "Nuevos Tickets"
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
                .height(75.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text (
                modifier = Modifier,
                text = "Nuevos Tickets",
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f), // x, y displacement in pixels
                        blurRadius = 4f          // how soft the shadow is
                    )
                )
            )
        }

        // Espacio de tickets Nuevos Tickets (No aceptados)

        Column (
            modifier = Modifier
                .weight(1f)
        ){
            TecnicoNonAcceptedTicketList(navController, userID, token, triggerAcceptedRefresh)
        }

        // Barra de navegacion

        /*
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
                //.height(100.dp)
                .background(Color.White)
        ){
            MenuScreen()
        }

         */

        /*lo comenté porque el appbar está en TecnicoMenuScreen.kt*/
//        Box (
//            modifier = Modifier
//                .fillMaxWidth()
//                .drawColoredShadow(
//                    color = Color.Black,           // Shadow color
//                    alpha = 0.25f,                 // Opacity of the shadow
//                    borderRadius = 8.dp,           // Match your box corner radius
//                    shadowRadius = 12.dp,          // Blur radius of the shadow
//                    offsetY = 4.dp,                // Vertical offset (shadow below the box)
//                    offsetX = 0.dp                 // Horizontal offset (centered)
//                )
//                .requiredHeightIn(max = 120.dp)
//                //.height(100.dp)
//                .background(Color.White)
//                .onGloballyPositioned { coordinates ->
//                    navBarSize = with(density) { coordinates.size.height.toDp() }
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment = Alignment.CenterVertically
//            ){
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Column (
//                    Modifier.size(notifIcon),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    IconButton(onClick = { /* TODO: Handle Home */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Home,
//                            contentDescription = "Inicio",
//                            tint = Color.Black
//                        )
//                    }
//                    Text("Inicio")
//                }
//
//                Spacer(modifier = Modifier.weight(3f))
//
//                Column (
//                    Modifier.onGloballyPositioned { coordinates ->
//                        notifIcon = with(density) { coordinates.size.height.toDp() }
//                    },
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    IconButton(onClick = { /* TODO: Handle Settings */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Notifications",
//                            tint = Color.Black
//                        )
//                    }
//                    Text("Notificaciones")
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//            }
//
//            LargeFloatingActionButton (
//                onClick = { /* TODO: Handle Search */ },
//                shape = CircleShape,
//                containerColor = Color.White,
//                contentColor = Color.Black,
//                modifier = Modifier
//                    .offset(y = (-navBarSize.value/1.5f).dp)
//                    .statusBarsPadding()
//                    .requiredHeightIn(min = 100.dp)
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    Icon(Icons.Default.Create, contentDescription = "Editar",
//                        modifier = Modifier
//                            .padding(bottom = 4.dp))
//                    Text("Editar")
//                }
//            }
//
//        }

        // Bottom Sheet
        /*

        BottomSheetTickets (
            showSheet = showSheet,
            onDismiss = { showSheet = false}
        )

        BottomSheetNew (
            showSheetNew = showSheetNew,
            onDismiss = { showSheetNew = false}
        )

        BottomSheetCreate(
            showSheetCreate = showSheetCreate,
            onDismiss = { showSheetCreate = false }
        )
        */
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropDown(
    label: String,
    options: List<String>,
    currentValue: String?,
    onValueSelected: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false)}

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight(align = Alignment.Top)
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(
                MenuAnchorType.PrimaryEditable,
                enabled = true
            ).fillMaxWidth(),
            readOnly = true,
            value = currentValue ?: label,
            onValueChange = {/* Read-only */},
            label = { Text(label, fontSize = 12.sp)},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.LightGray,
                focusedTrailingIconColor = Color.Black,
                unfocusedTrailingIconColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            shape = RoundedCornerShape(24.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption)},
                    onClick = {
                        onValueSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }

}