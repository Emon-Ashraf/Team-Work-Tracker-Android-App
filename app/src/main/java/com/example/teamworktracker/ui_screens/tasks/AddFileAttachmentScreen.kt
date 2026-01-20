package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFileAttachmentScreen(
    taskId: Int,
    onAttachmentAdded: () -> Unit,
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add File Attachment") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Task ID: $taskId", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            // ⚠️ For now we just show a stub. No real file picker yet (API/UI only).
            OutlinedButton(
                onClick = {
                    // Later: launch file picker and update selectedFileName
                    selectedFileName = "example_document.pdf"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose File (stub)")
            }

            Text(
                text = "Selected: ${selectedFileName ?: "No file selected"}",
                style = MaterialTheme.typography.bodySmall
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (selectedFileName == null) {
                        error = "Please select a file (stub)"
                    } else {
                        error = null
                        // Later: POST multipart /tasks/{task_id}/attachments/file
                        onAttachmentAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload File")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
