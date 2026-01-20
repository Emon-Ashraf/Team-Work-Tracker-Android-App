package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkAttachmentScreen(
    taskId: Int,
    onAttachmentAdded: () -> Unit,
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Link Attachment") }
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

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Link URL") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (url.isBlank()) {
                        error = "URL cannot be empty"
                    } else {
                        error = null
                        // Later: POST /tasks/{task_id}/attachments/link with { description, file_url: url }
                        onAttachmentAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Link")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
