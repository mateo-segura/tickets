package mx.tec.tickets.ui.screens

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlinx.coroutines.withContext
import mx.tec.tickets.model.ApiClient
import mx.tec.tickets.model.ChatApi
import mx.tec.tickets.model.MessageRequest



@Composable
fun FileScreen(ticketId: Int, userId: Int) {
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    val contexto = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { file ->
            selectedFile = file
            // Subir automáticamente después de seleccionar
            uploadFile(contexto, file, ticketId, userId) { uploaded, fileName, fileId ->
                if (uploaded) {
                    Toast.makeText(
                        contexto,
                        "Archivo '$fileName' subido correctamente (ID: $fileId)",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(contexto, "Error al subir el archivo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button(onClick = { filePicker.launch("*/*") }) {
                Text(text = "Seleccionar")
            }
            Button(
                onClick = {
                    selectedFile?.let { file ->
                        uploadFile(contexto, file, ticketId, userId) { _, _, _ -> }
                    }
                }
            ) {
                Text(text = "Cargar")
            }
        }

        selectedFile?.let { uri ->
            val fileName = getFileName(contexto, uri)
            Text(text = fileName)
        }

        HorizontalDivider(thickness = 1.dp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { downloadFile(contexto, "2") }) {
                Text(text = "Descargar")
            }
        }
    }
}


fun getFileName(context: Context, uri: Uri): String {
    var name = "archivo_desconocido"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) {
                name = it.getString(index)
            }
        }
    }
    return name
}

fun getFileSize(context: Context, uri: Uri): Long {
    var size: Long = 0
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (sizeIndex >= 0) size = cursor.getLong(sizeIndex)
        }
    }
    return size
}

fun downloadFile(context: Context, id: String){
    val url = "http://Api-tickets-env.eba-3z343hb2.us-east-1.elasticbeanstalk.com/archivos/download/$id"
    val fileName = "archivo_$id" // hacer esto dinámico
    // carpeta (lugar), nombre
    val file = File(context.getExternalFilesDir
        (Environment.DIRECTORY_DOWNLOADS) , fileName)

    val request = DownloadManager.Request(url.toUri())
        .setTitle(fileName)
        .setDescription("Descargando Archivo \uD83D\uDC31...PEZ")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(Uri.fromFile(file))

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

}

fun uploadFile(
    context: Context,
    uri: Uri,
    ticketId: Int,
    senderUserId: Int,
    onFileUploaded: (Boolean, String?, Int?) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return@launch

            // Copiamos el archivo a una ubicación temporal
            val tempFile = File(context.cacheDir, getFileName(context, uri))
            tempFile.outputStream().use { output -> inputStream.copyTo(output) }

            val fileName = getFileName(context, uri)
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())

            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .addFormDataPart("ticket_id", ticketId.toString())
                .addFormDataPart("sender_user_id", senderUserId.toString())
                .build()

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://Api-tickets-env.eba-3z343hb2.us-east-1.elasticbeanstalk.com/archivos/upload")
                .post(multipartBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val json = org.json.JSONObject(responseBody)
                        val fileId = json.optInt("file_id", -1)
                        if (fileId != -1) {
                            onFileUploaded(true, fileName, fileId)
                        } else {
                            onFileUploaded(false, null, null)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onFileUploaded(false, null, null)
                    }
                } else {
                    onFileUploaded(false, null, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) { onFileUploaded(false, null, null) }
        }
    }
}



@Composable
fun UploadButton(
    ticketId: Int,
    senderUserId: Int,
    onFileSent: (String, Int) -> Unit
) {
    val contexto = LocalContext.current
    var selectedFile by remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { file ->
            selectedFile = file
            uploadFile(contexto, file, ticketId, senderUserId) { uploaded, fileName, fileId ->
                if (uploaded && fileId != null) {
                    Toast.makeText(contexto, "Archivo subido correctamente", Toast.LENGTH_SHORT).show()
                    onFileSent(fileName ?: "Archivo", fileId)
                } else {
                    Toast.makeText(contexto, "Error al subir el archivo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Button(onClick = { filePicker.launch("*/*") }) {
        Text("📎")
    }
}


