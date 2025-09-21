package mx.tec.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.tec.tickets.ui.screens.MainScreen
import mx.tec.tickets.ui.theme.TicketsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketsTheme {
                MainScreen()
            }
        }
    }
}