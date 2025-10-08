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


@Composable
fun FileScreen() {
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    val contexto = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { file ->
            selectedFile = file
            // Subir automáticamente después de seleccionar
            uploadFile(contexto, file) { uploaded ->
                if (uploaded) {
                    Toast.makeText(contexto, "Archivo cargado correctamente", Toast.LENGTH_SHORT).show()
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
            Button(onClick = { filePicker.launch("*/*")}) {
                Text(text = "Seleccionar")
            }
            Button(
                onClick = { selectedFile?.let{ file ->
                    uploadFile(contexto, file){ uploaded -> }
                }
                }) {
                Text(text = "Cargar")
            }
        }
        selectedFile?.let {
                uri ->
            val filenName = getFileName(contexto, uri)
            Text(text= filenName)
        }

        HorizontalDivider(
            thickness = 1.dp
        )
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {downloadFile(contexto, "2")}) {
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
    val url = "http://10.0.2.2:3000/download/$id"
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

fun uploadFile(context: Context, uri: Uri, onFileUploaded:(Boolean)-> Unit ){
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return@launch

            // Guardamos el archivo en cache temporal
            val tempFile = File(context.cacheDir, "uploadFile.tmp")
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }

            val fileName = getFileName(context, uri)
            // Obtenemos el tipo MIME del archivo
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

            // Creamos el cuerpo del archivo
            val requestBody = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())


            // Construimos multipart
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .build()

            // Petición HTTP
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://10.0.2.2:3000/upload") // tu endpoint
                .post(multipartBody)
                .build()

            val response = client.newCall(request).execute()
            withContext(Dispatchers.Main) {
                onFileUploaded(response.isSuccessful)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            onFileUploaded(false)
        }
    }

}

@Composable
fun UploadButton(
    onFileSent: (String) -> Unit // 👈 Nuevo parámetro que manda el nombre del archivo
) {
    val contexto = LocalContext.current
    var selectedFile by remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { file ->
            selectedFile = file
            val fileName = getFileName(contexto, file)
            // Subir archivo
            uploadFile(contexto, file) { uploaded ->
                CoroutineScope(Dispatchers.Main).launch {
                    if (uploaded) {
                        Toast.makeText(contexto, "Archivo subido correctamente ✅", Toast.LENGTH_SHORT).show()
                        // 👇 Llamamos el callback para mostrarlo como mensaje
                        onFileSent(fileName)
                    } else {
                        Toast.makeText(contexto, "Error al subir el archivo ❌", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Button(onClick = { filePicker.launch("*/*") }) {
        Text(text = "📎 Archivo")
    }
}

