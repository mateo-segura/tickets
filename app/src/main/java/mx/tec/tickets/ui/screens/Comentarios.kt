package mx.tec.chat
import android.content.Context
import mx.tec.tickets.ui.screens.UploadButton
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.tec.tickets.model.ChatRepository.fetchMessagesByTicket
import mx.tec.tickets.model.ChatRepository.sendMessage
import mx.tec.tickets.ui.screens.FileScreen
import mx.tec.tickets.ui.screens.downloadFile
import mx.tec.tickets.ui.screens.UploadButton
import mx.tec.tickets.ui.screens.downloadFile
import mx.tec.tickets.ui.theme.TicketsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketsTheme {



            }
        }
    }
}

data class Message(
    val author: String,
    val body: String,
    val isUser: Boolean = false,
    val fileId: String? = null //
)


@Composable
fun MessageCard(msg: Message) {
    val contexto = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        Column {
            if (!msg.isUser) {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            val surfaceColor by animateColorAsState(
                if (msg.isUser) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
                    // 👇 solo clickeable si es archivo
                    .let {
                        if (msg.fileId != null)
                            it.clickable() {
                                downloadFile(contexto, msg.fileId)
                            }
                        else it
                    }
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (msg.isUser) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        state = listState,
        reverseLayout = true
    ) {
        items(messages) { message ->
            MessageCard(message)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }
}

@Composable
fun ChatScreen(
    context: Context = LocalContext.current,
    ticketId: Int,
    userId: Int,
    token: String // <-- asegúrate de pasar el token aquí
) {
    val messages = remember { mutableStateListOf<Message>() }
    var currentText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Cargar mensajes al inicio
    LaunchedEffect(ticketId,userId) {
        fetchMessagesByTicket(
            context = context,
            ticketId = ticketId,
            userId,
            token = token
        ) { fetchedMessages ->
            messages.clear()
            messages.addAll(fetchedMessages)
        }
    }

    // UI del chat
    Scaffold(
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje...") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // --- BOTÓN DE SUBIR ARCHIVO ---
                UploadButton(
                    ticketId = ticketId,
                    senderUserId = userId,
                    onFileSent = { fileName, fileId ->
                        // Esto se ejecuta cuando el archivo se sube con éxito

                        val fileBody = "📎 $fileName"

                        // 1. Añadimos localmente para que la UI sea rápida
                        messages.add(
                            0,
                            Message(
                                author = "Tú",
                                body = fileBody,
                                isUser = true,
                                fileId = fileId.toString()
                            )
                        )

                        // 2. AHORA SÍ: Guardamos el mensaje en la base de datos
                        sendMessage(
                            context = context,
                            ticketId = ticketId,
                            senderUserId = userId,
                            body = fileBody, // "📎 nombre_archivo.jpg"
                            token = token,
                            fileId = fileId, // <-- Pasamos el ID del archivo
                            onSuccess = {
                                Log.d("ChatScreen", "Mensaje de archivo guardado en DB")
                            },
                            onError = { errorMsg ->
                                Log.e("ChatScreen", "Error al guardar mensaje de archivo: $errorMsg")
                            }
                        )
                    }
                )
                // --- FIN DEL BOTÓN DE ARCHIVO ---

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de envío de TEXTO (usa el mismo sendMessage, pero sin fileId)
                Button(
                    onClick = {
                        if (currentText.isNotBlank()) {
                            val textToSend = currentText
                            currentText = ""

                            // Mostrar mensaje localmente
                            messages.add(
                                0,
                                Message(
                                    author = "Tú",
                                    body = textToSend,
                                    isUser = true
                                )
                            )

                            // Enviar mensaje al servidor (sin fileId)
                            sendMessage(
                                context = context,
                                ticketId = ticketId,
                                senderUserId = userId,
                                body = textToSend,
                                token = token,
                                fileId = null, // <-- No es un archivo
                                onSuccess = {
                                    Log.d("ChatScreen", "Mensaje de texto enviado")
                                },
                                onError = { errorMsg ->
                                    Log.e("ChatScreen", "Error al enviar mensaje: $errorMsg")
                                }
                            )
                        }
                    }
                ) {
                    Text("Enviar")
                }
            }
        }
    ) { paddingValues ->
        Conversation(
            messages = messages,
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
        )
    }
}

/*
@Composable
fun ChatScreen(ticketId: Int, userId: Int) {
    FileScreen(ticketId, userId)

    val messages = remember { mutableStateListOf<Message>() }
    var currentText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Cargar mensajes al inicio
    LaunchedEffect(ticketId) {

        val api = mx.tec.tickets.model.ApiClient
            .retrofit
            .create(mx.tec.tickets.model.ChatApi::class.java)

        try {
            val response = api.getMessagesByTicket(ticketId)
            if (response.isSuccessful) {
                val messageList = response.body().orEmpty()
                messages.clear()
                messages.addAll(
                    messageList.map {
                        Message(
                            author = if (it.sender_user_id == userId) "Tú" else "Agente",
                            body = it.body,
                            isUser = it.sender_user_id == userId
                        )
                    }.reversed() // Para mostrar de más antiguo a más nuevo
                )
            } else {
                println("Error al obtener mensajes: ${response.code()}")
            }
        } catch (e: Exception) {
            println("Error de red: ${e.message}")
        }
    }

    // UI del chat
    Scaffold(
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                //Boton para subir archivos
                UploadButton(
                    ticketId = ticketId,
                    senderUserId = userId,
                    onFileSent = { fileName, fileId ->
                        // Mostrar mensaje con archivo subido
                        messages.add(0, Message("Tú", "📎 $fileName", isUser = true, fileId = fileId.toString()))
                    }
                )


                Button(
                    onClick = {
                        if (currentText.isNotBlank()) {
                            val textToSend = currentText
                            currentText = ""

                            // Mostrar mensaje localmente
                            messages.add(0, Message("Tú", textToSend, isUser = true))

                            // Enviar mensaje al servidor
                            coroutineScope.launch {
                                val api = mx.tec.tickets.model.ApiClient
                                    .retrofit
                                    .create(mx.tec.tickets.model.ChatApi::class.java)

                                try {
                                    val response = api.sendMessage(
                                        mx.tec.tickets.model.MessageRequest(
                                            ticket_id = ticketId,
                                            sender_user_id = userId,
                                            body = textToSend
                                        )
                                    )

                                    if (!response.isSuccessful) {
                                        println("Error al enviar mensaje: ${response.code()}")
                                    }
                                } catch (e: Exception) {
                                    println("Error de red: ${e.message}")
                                }
                            }
                        }
                    }
                ) {
                    Text("Enviar")
                }
            }
        }
    ) { paddingValues ->
        Conversation(
            messages = messages,
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
        )
    }
}
*/

/* @Composable
fun ChatScreen() {
    val messages = remember { SampleData.conversationSample.toMutableStateList() }
    var currentText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope() //

    Scaffold(
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                UploadButton(
                    onFileSent = { fileName ->
                        messages.add(
                            0,
                            Message(
                                author = "You",
                                body = "📎 $fileName",
                                isUser = true,
                                fileId = "11"
                            )
                        )
                    }
                )
                Button(
                    onClick = {
                        if (currentText.isNotBlank()) {
                            messages.add(0, Message("You", currentText, isUser = true))
                            val textToSend = currentText
                            currentText = ""


                            coroutineScope.launch {
                                val api = mx.tec.tickets.model.ApiClient
                                    .retrofit
                                    .create(mx.tec.tickets.model.ChatApi::class.java)

                                try {
                                    val response = api.sendMessage(
                                        mx.tec.tickets.model.MessageRequest(
                                            ticket_id = 1,
                                            sender_user_id = 2,
                                            body = textToSend
                                        )
                                    )

                                    if (response.isSuccessful) {
                                        println("Mensaje enviado correctamente")
                                    } else {
                                        println("Error del servidor: ${response.code()}")
                                    }

                                } catch (e: Exception) {
                                    println("Error de red: ${e.message}")
                                }
                            }
                        }
                    }
                ) {
                    Text("Enviar")
                }
            }
        }
    ) { paddingValues ->
        Conversation(
            messages = messages,
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
                .fillMaxSize()
        )
    }
}
*/
/*
@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    TicketsTheme {
        ChatScreen()
    }
}
*/