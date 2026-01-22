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
fun CreateTaskScreen(
    onTaskCreated: () -> Unit,
    onBack: () -> Unit,
    viewModel: MyTasksViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var projectIdText by remember { mutableStateOf("") }
    var assignedToText by remember { mutableStateOf("") }

    val priorityOptions = listOf("low", "medium", "high")
    var selectedPriority by remember { mutableStateOf("medium") }
    var priorityExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("new", "in_progress", "completed", "blocked")
    var selectedStatus by remember { mutableStateOf("new") }
    var statusExpanded by remember { mutableStateOf(false) }

    var dueDateText by remember { mutableStateOf("") } // backend expects string; use ISO datetime ideally
    var localError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Create Task") })
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
                label = { Text("Due date (ISO recommended)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("2026-01-30T10:00:00") }
            )

            val shownError = localError ?: uiState.error
            if (shownError != null) {
                Text(text = shownError, color = MaterialTheme.colorScheme.error)
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            Button(
                onClick = {
                    val projectId = projectIdText.toIntOrNull()
                    val assignedTo = assignedToText.toIntOrNull()

                    when {
                        title.isBlank() -> localError = "Title is required"
                        projectId == null -> localError = "Valid project ID is required"
                        assignedTo == null -> localError = "Valid assignee ID is required"
                        dueDateText.isBlank() -> localError = "Due date is required"
                        else -> {
                            localError = null
                            viewModel.createTask(
                                projectId = projectId,
                                assignedTo = assignedTo,
                                title = title.trim(),
                                description = description.trim(),
                                priority = selectedPriority,
                                status = selectedStatus,
                                dueDate = dueDateText.trim(),
                                onSuccess = onTaskCreated
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Create Task")
            }

            TextButton(
                onClick = onBack,
                enabled = !uiState.isLoading
            ) {
                Text("Cancel")
            }
        }
    }
}
