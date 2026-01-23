package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.ui.theme.AppColors

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

    var dueDateText by remember { mutableStateOf("") } //
    var localError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Task") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppColors.BgDark,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        // ✅ scrollable content + bottom actions
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0B0F2B), AppColors.BgDark, AppColors.BgDark)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SectionTitle("Task info")

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    minLines = 3,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                SectionTitle("IDs")

                OutlinedTextField(
                    value = projectIdText,
                    onValueChange = { projectIdText = it },
                    label = { Text("Project ID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = assignedToText,
                    onValueChange = { assignedToText = it },
                    label = { Text("Assigned To (user id)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                SectionTitle("Attributes")

                // Priority dropdown (same logic)
                ExposedDropdownMenuBox(
                    expanded = priorityExpanded,
                    onExpandedChange = { if (!uiState.isLoading) priorityExpanded = !priorityExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedPriority,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        colors = fieldColors(),
                        shape = RoundedCornerShape(14.dp)
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

                // Status dropdown (same logic)
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { if (!uiState.isLoading) statusExpanded = !statusExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        enabled = !uiState.isLoading,
                        colors = fieldColors(),
                        shape = RoundedCornerShape(14.dp)
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

                SectionTitle("Deadline")

                OutlinedTextField(
                    value = dueDateText,
                    onValueChange = { dueDateText = it },
                    label = { Text("Due date (ISO recommended)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    placeholder = { Text("2026-01-30T10:00:00", color = AppColors.MutedText) },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Errors (same logic)
                val shownError = localError ?: uiState.error
                if (shownError != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = shownError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                if (uiState.isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.PrimaryBlue)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Bottom actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Create Task")
                }

                TextButton(
                    onClick = onBack,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = AppColors.MutedText)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = AppColors.FieldBg,
    unfocusedContainerColor = AppColors.FieldBg,
    disabledContainerColor = AppColors.FieldBg.copy(alpha = 0.6f),

    focusedBorderColor = AppColors.PrimaryBlue,
    unfocusedBorderColor = AppColors.Border,
    disabledBorderColor = AppColors.Border.copy(alpha = 0.5f),

    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.7f),

    cursorColor = AppColors.PrimaryBlue
)
