package mx.tec.tickets.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.tickets.ui.theme.drawColoredShadow

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VistaPrincipalTecnico() {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

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
                    .padding(16.dp)
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                text = "Mis Tickets",
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f), // x, y displacement in pixels
                        blurRadius = 4f          // how soft the shadow is
                    )
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            val buttonWidth = 120.dp
            val buttonHeight = 35.dp

            Button (
                onClick = { /* TODO: Categoria */ },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth,buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,           // Shadow color
                        alpha = 0.25f,                 // Opacity of the shadow
                        borderRadius = 8.dp,           // Match your box corner radius
                        shadowRadius = 12.dp,          // Blur radius of the shadow
                        offsetY = 4.dp,                // Vertical offset (shadow below the box)
                        offsetX = 0.dp                 // Horizontal offset (centered)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Categoria")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Categoria", fontSize = 12.sp)
            }
            Button (
                onClick = { /* TODO: Prioridad */ },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth,buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,           // Shadow color
                        alpha = 0.25f,                 // Opacity of the shadow
                        borderRadius = 8.dp,           // Match your box corner radius
                        shadowRadius = 12.dp,          // Blur radius of the shadow
                        offsetY = 4.dp,                // Vertical offset (shadow below the box)
                        offsetX = 0.dp                 // Horizontal offset (centered)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Prioridad")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Prioridad", fontSize = 12.sp)
            }
            Button (
                onClick = { /* TODO: Fecha */ },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .size(buttonWidth,buttonHeight)
                    .padding(horizontal = 4.dp)
                    .drawColoredShadow(
                        color = Color.Black,           // Shadow color
                        alpha = 0.25f,                 // Opacity of the shadow
                        borderRadius = 8.dp,           // Match your box corner radius
                        shadowRadius = 12.dp,          // Blur radius of the shadow
                        offsetY = 4.dp,                // Vertical offset (shadow below the box)
                        offsetX = 0.dp                 // Horizontal offset (centered)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Fecha")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fecha", fontSize = 12.sp)
            }
        }

        Spacer (
            modifier = Modifier
                .weight(1f)
        )

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

        Spacer (
            modifier = Modifier
                .weight(1f)
        )

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
                .height(100.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { /* TODO: Handle Home */ }) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { /* TODO: Handle Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { /* TODO: Handle Settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}