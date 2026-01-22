package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkAttachmentScreen(
    taskId: Int,
    onAttachmentAdded: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddAttachmentViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    var description by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.done) {
        if (uiState.done) onAttachmentAdded()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Add Link Attachment") }) }
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

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Link URL") },
                modifier = Modifier.fillMaxWidth()
            )

            val shownError = localError ?: uiState.error
            if (shownError != null) {
                Text(shownError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (url.isBlank()) {
                        localError = "URL cannot be empty"
                        return@Button
                    }
                    localError = null

                    viewModel.addLink(
                        taskId = taskId,
                        url = url.trim(),
                        description = description.trim()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Adding..." else "Add Link")
            }

            TextButton(onClick = onBack) { Text("Cancel") }
        }
    }
}
