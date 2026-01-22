package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentScreen(
    taskId: Int,
    onCommentAdded: () -> Unit,
    onBack: () -> Unit,
    viewModel: AddCommentViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()
    var content by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.done) {
        if (uiState.done) onCommentAdded()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Add Comment") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Task ID: $taskId", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Comment") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                maxLines = 5
            )

            val shownError = localError ?: uiState.error
            if (shownError != null) {
                Text(shownError, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (content.isBlank()) {
                        localError = "Comment cannot be empty"
                        return@Button
                    }
                    localError = null
                    viewModel.submit(taskId, content.trim())
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Adding..." else "Add Comment")
            }

            TextButton(onClick = onBack) { Text("Cancel") }
        }
    }
}
