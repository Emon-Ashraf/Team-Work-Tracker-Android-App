package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: Int,
    onBack: () -> Unit,
    onEditTask: (Int) -> Unit,
    onAddComment: (Int) -> Unit,
    onAddAttachmentLink: (Int) -> Unit,
    onAddAttachmentFile: (Int) -> Unit,
    viewModel: TaskDetailsViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ✅ load from backend
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    // ✅ if deleted, go back
    LaunchedEffect(uiState.deleted) {
        if (uiState.deleted) onBack()
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete task?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteTask(taskId)
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadTask(taskId) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete task")
                    }
                }
            )
        }
    ) { padding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)

                    Button(onClick = { viewModel.loadTask(taskId) }) {
                        Text("Retry")
                    }
                }
            }

            uiState.task == null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Task not found", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onBack) { Text("Back") }
                }
            }

            else -> {
                val task = uiState.task!!

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // === Basic details ===
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(task.title, style = MaterialTheme.typography.headlineSmall)
                        Text(task.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Project ID: ${task.projectId}")
                        Text("Assigned to (user id): ${task.assignedTo}")
                        Text("Status: ${task.status.name}")
                        Text("Priority: ${task.priority.name}")
                        Text("Due date: ${task.dueDate}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Created by: ${task.createdBy}")
                        Text("Created at: ${task.createdAt}")
                        Text("Updated at: ${task.updatedAt}")
                    }

                    Button(
                        onClick = { onEditTask(task.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Task")
                    }

                    HorizontalDivider()

                    // === Comments section (next step: wire API) ===
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Comments", style = MaterialTheme.typography.titleMedium)

                        Text(
                            text = "Comments will show here after we connect: GET /api/v1/tasks/{task_id}/comments",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Button(
                            onClick = { onAddComment(task.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add Comment")
                        }
                    }

                    HorizontalDivider()

                    // === Attachments section (next step: wire API) ===
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Attachments", style = MaterialTheme.typography.titleMedium)

                        Text(
                            text = "Attachments will show here after we connect: GET /api/v1/tasks/{task_id}/attachments",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onAddAttachmentFile(task.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Add File")
                            }
                            OutlinedButton(
                                onClick = { onAddAttachmentLink(task.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Add Link")
                            }
                        }
                    }

                    HorizontalDivider()

                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back")
                    }
                }
            }
        }
    }
}
