package mx.tec.chat
import mx.tec.tickets.ui.screens.UploadButton
import android.os.Bundle
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
import mx.tec.tickets.ui.screens.downloadFile
import mx.tec.tickets.ui.screens.UploadButton
import mx.tec.tickets.ui.screens.downloadFile
import mx.tec.tickets.ui.theme.TicketsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicketsTheme {
                ChatScreen()
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
fun ChatScreen() {
    val messages = remember { SampleData.conversationSample.toMutableStateList() }
    var currentText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope() // ✅ se agrega esta línea

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


@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    TicketsTheme {
        ChatScreen()
    }
}
