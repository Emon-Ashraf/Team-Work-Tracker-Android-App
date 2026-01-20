package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentScreen(
    taskId: Int,
    onCommentAdded: () -> Unit,
    onBack: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Comment") }
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

            Text(
                text = "Task ID: $taskId",
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Comment") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 5
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (content.isBlank()) {
                        error = "Comment cannot be empty"
                    } else {
                        error = null
                        // Later: POST /api/v1/tasks/{task_id}/comments with { content }
                        onCommentAdded()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Comment")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
