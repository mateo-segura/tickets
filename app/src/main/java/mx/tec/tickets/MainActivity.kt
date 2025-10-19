package mx.tec.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.tec.tickets.navigation.AppNavigation
import mx.tec.tickets.ui.theme.TicketsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketsTheme {

                AppNavigation() // diagrama (componente ticket): https://drive.google.com/file/d/156gHLbHn6pczxFrlbUp2ROohhLyCS1jl/view?usp=sharing
                // MainScreen() // AppBar archivos: https://drive.google.com/file/d/1UDDoKUqLj_I_v5XTkAVQWeb9VmkESt3I/view?usp=sharing
                // VistaInteriorTicket()
                // VistaInteriorTicket()
                // ChatScreen()
                // Comentario de Prueba parp probar el github UwU

            }
        }
    }
}