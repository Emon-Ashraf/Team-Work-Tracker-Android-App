package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    onTaskUpdated: () -> Unit,
    onBack: () -> Unit,
    viewModel: EditTaskViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(taskId) {
        viewModel.load(taskId)
    }

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onTaskUpdated()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Edit Task") }) }
    ) { padding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) { CircularProgressIndicator() }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.load(taskId) }) { Text("Retry") }
                    TextButton(onClick = onBack) { Text("Back") }
                }
            }

            uiState.task == null -> {
                Column(
                    modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Task not found", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onBack) { Text("Back") }
                }
            }

            else -> {
                val task = uiState.task!!

                var title by remember(task.id) { mutableStateOf(task.title) }
                var description by remember(task.id) { mutableStateOf(task.description) }
                var assignedToText by remember(task.id) { mutableStateOf(task.assignedTo.toString()) }
                var selectedPriority by remember(task.id) { mutableStateOf(task.priority.name.lowercase()) }
                var selectedStatus by remember(task.id) { mutableStateOf(task.status.name.lowercase()) }
                var dueDateText by remember(task.id) { mutableStateOf(task.dueDate) }

                val priorityOptions = listOf("low", "medium", "high")
                val statusOptions = listOf("new", "in_progress", "completed", "blocked")

                var priorityExpanded by remember { mutableStateOf(false) }
                var statusExpanded by remember { mutableStateOf(false) }
                var localError by remember { mutableStateOf<String?>(null) }

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = assignedToText,
                        onValueChange = { assignedToText = it },
                        label = { Text("Assigned To (user id)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = priorityExpanded,
                        onExpandedChange = { priorityExpanded = !priorityExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPriority,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Priority") },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false }
                        ) {
                            priorityOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedPriority = option
                                        priorityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedStatus,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedStatus = option
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = dueDateText,
                        onValueChange = { dueDateText = it },
                        label = { Text("Due date (ISO)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    val shownError = localError ?: uiState.error
                    if (shownError != null) {
                        Text(shownError, color = MaterialTheme.colorScheme.error)
                    }

                    Button(
                        onClick = {
                            val assignedTo = assignedToText.toIntOrNull()
                            if (title.isBlank()) {
                                localError = "Title is required"
                                return@Button
                            }
                            if (assignedTo == null) {
                                localError = "Valid assignee ID is required"
                                return@Button
                            }
                            localError = null

                            viewModel.save(
                                taskId = taskId,
                                body = mapOf(
                                    "title" to title.trim(),
                                    "description" to description.trim(),
                                    "assigned_to" to assignedTo,
                                    "priority" to selectedPriority,
                                    "status" to selectedStatus,
                                    "due_date" to dueDateText.trim()
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isSaving
                    ) {
                        Text(if (uiState.isSaving) "Saving..." else "Save changes")
                    }

                    TextButton(onClick = onBack) { Text("Cancel") }
                }
            }
        }
    }
}
