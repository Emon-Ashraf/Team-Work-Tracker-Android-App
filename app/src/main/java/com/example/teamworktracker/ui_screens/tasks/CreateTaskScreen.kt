package com.example.teamworktracker.ui_screens.tasks

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme
import com.example.teamworktracker.ui_screens.preview.PreviewData

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

    var selectedPriority by remember { mutableStateOf("medium") }
    var priorityExpanded by remember { mutableStateOf(false) }

    var selectedStatus by remember { mutableStateOf("new") }
    var statusExpanded by remember { mutableStateOf(false) }

    var dueDateText by remember { mutableStateOf("") } //
    var localError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    CreateTaskScreenContent(
        uiState = uiState,
        title = title,
        onTitleChange = { title = it },
        description = description,
        onDescriptionChange = { description = it },
        projectIdText = projectIdText,
        onProjectIdTextChange = { projectIdText = it },
        assignedToText = assignedToText,
        onAssignedToTextChange = { assignedToText = it },
        selectedPriority = selectedPriority,
        onSelectedPriorityChange = { selectedPriority = it },
        priorityExpanded = priorityExpanded,
        onPriorityExpandedChange = { priorityExpanded = it },
        selectedStatus = selectedStatus,
        onSelectedStatusChange = { selectedStatus = it },
        statusExpanded = statusExpanded,
        onStatusExpandedChange = { statusExpanded = it },
        dueDateText = dueDateText,
        onDueDateTextChange = { dueDateText = it },
        localError = localError,
        onLocalErrorChange = { localError = it },
        onCreateTask = { projectId, assignedTo, title, description, priority, status, dueDate ->
            viewModel.createTask(projectId, assignedTo, title, description, priority, status, dueDate, onTaskCreated)
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreenContent(
    uiState: MyTasksUiState,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    projectIdText: String,
    onProjectIdTextChange: (String) -> Unit,
    assignedToText: String,
    onAssignedToTextChange: (String) -> Unit,
    selectedPriority: String,
    onSelectedPriorityChange: (String) -> Unit,
    priorityExpanded: Boolean,
    onPriorityExpandedChange: (Boolean) -> Unit,
    selectedStatus: String,
    onSelectedStatusChange: (String) -> Unit,
    statusExpanded: Boolean,
    onStatusExpandedChange: (Boolean) -> Unit,
    dueDateText: String,
    onDueDateTextChange: (String) -> Unit,
    localError: String?,
    onLocalErrorChange: (String?) -> Unit,
    onCreateTask: (Int, Int, String, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val priorityOptions = listOf("low", "medium", "high")
    val statusOptions = listOf("new", "in_progress", "completed", "blocked")

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
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
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
                    onValueChange = onProjectIdTextChange,
                    label = { Text("Project ID") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = assignedToText,
                    onValueChange = onAssignedToTextChange,
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
                    onExpandedChange = { if (!uiState.isLoading) onPriorityExpandedChange(!priorityExpanded) }
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
                        onDismissRequest = { onPriorityExpandedChange(false) }
                    ) {
                        priorityOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onSelectedPriorityChange(option)
                                    onPriorityExpandedChange(false)
                                }
                            )
                        }
                    }
                }

                // Status dropdown (same logic)
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { if (!uiState.isLoading) onStatusExpandedChange(!statusExpanded) }
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
                        onDismissRequest = { onStatusExpandedChange(false) }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onSelectedStatusChange(option)
                                    onStatusExpandedChange(false)
                                }
                            )
                        }
                    }
                }

                SectionTitle("Deadline")

                OutlinedTextField(
                    value = dueDateText,
                    onValueChange = onDueDateTextChange,
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
                            title.isBlank() -> onLocalErrorChange("Title is required")
                            projectId == null -> onLocalErrorChange("Valid project ID is required")
                            assignedTo == null -> onLocalErrorChange("Valid assignee ID is required")
                            dueDateText.isBlank() -> onLocalErrorChange("Due date is required")
                            else -> {
                                onLocalErrorChange(null)
                                onCreateTask(
                                    projectId, assignedTo, title.trim(), description.trim(),
                                    selectedPriority, selectedStatus, dueDateText.trim()
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

@Preview(name = "Light", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateTaskScreenPreview() {
    TeamWorkTrackerTheme(dynamicColor = false) {
        CreateTaskScreenContent(
            uiState = MyTasksUiState(),
            title = PreviewData.task.title,
            onTitleChange = {},
            description = PreviewData.task.description,
            onDescriptionChange = {},
            projectIdText = "1",
            onProjectIdTextChange = {},
            assignedToText = "12",
            onAssignedToTextChange = {},
            selectedPriority = "medium",
            onSelectedPriorityChange = {},
            priorityExpanded = false,
            onPriorityExpandedChange = {},
            selectedStatus = "new",
            onSelectedStatusChange = {},
            statusExpanded = false,
            onStatusExpandedChange = {},
            dueDateText = PreviewData.task.dueDate,
            onDueDateTextChange = {},
            localError = null,
            onLocalErrorChange = {},
            onCreateTask = { _, _, _, _, _, _, _ -> },
            onBack = {}
        )
    }
}
