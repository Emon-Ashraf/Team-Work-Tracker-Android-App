package com.example.teamworktracker.ui_screens.tasks

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFileAttachmentScreen(
    taskId: Int,
    onAttachmentAdded: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddAttachmentViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    var description by remember { mutableStateOf("") }
    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var localError by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        pickedUri = uri
    }

    LaunchedEffect(uiState.done) {
        if (uiState.done) onAttachmentAdded()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Add File Attachment") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Task ID: $taskId", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = { picker.launch(arrayOf("*/*")) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose File")
            }

            Text(
                text = if (pickedUri != null) "Selected: $pickedUri" else "No file selected",
                style = MaterialTheme.typography.bodySmall
            )

            val shownError = localError ?: uiState.error
            if (shownError != null) {
                Text(shownError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    val uri = pickedUri
                    if (uri == null) {
                        localError = "Please select a file"
                        return@Button
                    }
                    localError = null
                    viewModel.uploadFile(
                        context = context,
                        taskId = taskId,
                        uri = uri,
                        description = description.trim()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Uploading..." else "Upload File")
            }

            TextButton(onClick = onBack) { Text("Cancel") }
        }
    }
}
