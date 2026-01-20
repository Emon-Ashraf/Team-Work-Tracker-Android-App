package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.data.FakeTaskRepository
import com.example.teamworktracker.domain.models.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    onTaskUpdated: () -> Unit,
    onBack: () -> Unit
) {
    val task = FakeTaskRepository.getTaskById(taskId)

    if (task == null) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Edit Task") }
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
                Text("Task not found", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onBack) {
                    Text("Back")
                }
            }
        }
        return
    }

    // Prefilled state from existing task
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var projectIdText by remember { mutableStateOf(task.projectId.toString()) }
    var assignedToText by remember { mutableStateOf(task.assignedTo.toString()) }

    val priorityOptions = listOf("low", "medium", "high")
    var selectedPriority by remember {
        mutableStateOf(task.priority.name.lowercase())
    }
    var priorityExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("new", "in_progress", "completed", "blocked")
    var selectedStatus by remember {
        mutableStateOf(
            when (task.status) {
                TaskStatus.NEW -> "new"
                TaskStatus.IN_PROGRESS -> "in_progress"
                TaskStatus.COMPLETED -> "completed"
                TaskStatus.BLOCKED -> "blocked"
            }
        )
    }
    var statusExpanded by remember { mutableStateOf(false) }

    var dueDateText by remember { mutableStateOf(task.dueDate) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Task") }
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
                value = projectIdText,
                onValueChange = { projectIdText = it },
                label = { Text("Project ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = assignedToText,
                onValueChange = { assignedToText = it },
                label = { Text("Assigned To (user id)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Priority dropdown
            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPriority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            // Status dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = selectedStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
                label = { Text("Due date (ISO or simple)") },
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
                    val projectId = projectIdText.toIntOrNull()
                    val assignedTo = assignedToText.toIntOrNull()

                    if (title.isBlank()) {
                        error = "Title is required"
                    } else if (projectId == null) {
                        error = "Valid project ID is required"
                    } else if (assignedTo == null) {
                        error = "Valid assignee ID is required"
                    } else {
                        error = null
                        // Later: PUT /api/v1/tasks/{taskId} with updated fields
                        onTaskUpdated()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save changes")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
