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
import androidx.compose.ui.Alignment
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

    val task = uiState.task
    val formState = if (!uiState.isLoading && uiState.error == null && task != null) {
        remember(task.id) {
            mutableStateOf(
                EditTaskFormState(
                    title = task.title,
                    description = task.description,
                    assignedToText = task.assignedTo.toString(),
                    selectedPriority = task.priority.name.lowercase(),
                    selectedStatus = task.status.name.lowercase(),
                    dueDateText = task.dueDate
                )
            )
        }
    } else {
        null
    }

    EditTaskScreenContent(
        uiState = uiState,
        form = formState?.value ?: EditTaskFormState(),
        onFormChange = { formState?.value = it },
        onRetry = { viewModel.load(taskId) },
        onSave = { viewModel.save(taskId, it) },
        onBack = onBack
    )
}

data class EditTaskFormState(
    val title: String = "",
    val description: String = "",
    val assignedToText: String = "",
    val selectedPriority: String = "",
    val selectedStatus: String = "",
    val dueDateText: String = "",
    val priorityExpanded: Boolean = false,
    val statusExpanded: Boolean = false,
    val localError: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreenContent(
    uiState: EditTaskUiState,
    form: EditTaskFormState,
    onFormChange: (EditTaskFormState) -> Unit,
    onRetry: () -> Unit,
    onSave: (Map<String, Any?>) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Task") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppColors.BgDark,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        val bg = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0B0F2B), AppColors.BgDark, AppColors.BgDark)
                )
            )

        when {
            uiState.isLoading -> {
                Box(modifier = bg, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.PrimaryBlue)
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = bg.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Button(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                    ) { Text("Retry") }

                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back", color = AppColors.MutedText)
                    }
                }
            }

            uiState.task == null -> {
                Column(
                    modifier = bg.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Task not found", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back", color = AppColors.MutedText)
                    }
                }
            }

            else -> {
                val title = form.title
                val description = form.description
                val assignedToText = form.assignedToText
                val selectedPriority = form.selectedPriority
                val selectedStatus = form.selectedStatus
                val dueDateText = form.dueDateText
                val priorityExpanded = form.priorityExpanded
                val statusExpanded = form.statusExpanded
                val localError = form.localError
                val priorityOptions = listOf("low", "medium", "high")
                val statusOptions = listOf("new", "in_progress", "completed", "blocked")

                Column(
                    modifier = bg.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Update details", color = Color.White, style = MaterialTheme.typography.titleMedium)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { onFormChange(form.copy(title = it)) },
                                    label = { Text("Title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isSaving,
                                    colors = fieldColors(),
                                    shape = RoundedCornerShape(14.dp)
                                )

                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { onFormChange(form.copy(description = it)) },
                                    label = { Text("Description") },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isSaving,
                                    minLines = 3,
                                    colors = fieldColors(),
                                    shape = RoundedCornerShape(14.dp)
                                )

                                OutlinedTextField(
                                    value = assignedToText,
                                    onValueChange = { onFormChange(form.copy(assignedToText = it)) },
                                    label = { Text("Assigned To (user id)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isSaving,
                                    colors = fieldColors(),
                                    shape = RoundedCornerShape(14.dp)
                                )

                                ExposedDropdownMenuBox(
                                    expanded = priorityExpanded,
                                    onExpandedChange = { if (!uiState.isSaving) onFormChange(form.copy(priorityExpanded = !priorityExpanded)) }
                                ) {
                                    OutlinedTextField(
                                        value = selectedPriority,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Priority") },
                                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                                        enabled = !uiState.isSaving,
                                        colors = fieldColors(),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = priorityExpanded,
                                        onDismissRequest = { onFormChange(form.copy(priorityExpanded = false)) }
                                    ) {
                                        priorityOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    onFormChange(form.copy(selectedPriority = option, priorityExpanded = false))
                                                }
                                            )
                                        }
                                    }
                                }

                                ExposedDropdownMenuBox(
                                    expanded = statusExpanded,
                                    onExpandedChange = { if (!uiState.isSaving) onFormChange(form.copy(statusExpanded = !statusExpanded)) }
                                ) {
                                    OutlinedTextField(
                                        value = selectedStatus,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Status") },
                                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                                        enabled = !uiState.isSaving,
                                        colors = fieldColors(),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = statusExpanded,
                                        onDismissRequest = { onFormChange(form.copy(statusExpanded = false)) }
                                    ) {
                                        statusOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    onFormChange(form.copy(selectedStatus = option, statusExpanded = false))
                                                }
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = dueDateText,
                                    onValueChange = { onFormChange(form.copy(dueDateText = it)) },
                                    label = { Text("Due date (ISO)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !uiState.isSaving,
                                    colors = fieldColors(),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }
                        }

                        val shownError = localError ?: uiState.error
                        if (shownError != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    shownError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }

                        if (uiState.isSaving) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = AppColors.PrimaryBlue,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                val assignedTo = assignedToText.toIntOrNull()
                                when {
                                    title.isBlank() -> {
                                        onFormChange(form.copy(localError = "Title is required"))
                                        return@Button
                                    }
                                    assignedTo == null -> {
                                        onFormChange(form.copy(localError = "Valid assignee ID is required"))
                                        return@Button
                                    }
                                    else -> onFormChange(form.copy(localError = null))
                                }

                                onSave(
                                    mapOf(
                                        "title" to title.trim(),
                                        "description" to description.trim(),
                                        "assigned_to" to assignedTo,
                                        "priority" to selectedPriority,
                                        "status" to selectedStatus,
                                        "due_date" to dueDateText.trim()
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            enabled = !uiState.isSaving,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                        ) {
                            Text(if (uiState.isSaving) "Saving..." else "Save changes")
                        }

                        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                            Text("Cancel", color = AppColors.MutedText)
                        }
                    }
                }
            }
        }
    }
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
private fun EditTaskScreenPreview() {
    TeamWorkTrackerTheme(dynamicColor = false) {
        EditTaskScreenContent(
            uiState = EditTaskUiState(task = PreviewData.task),
            form = EditTaskFormState(
                title = PreviewData.task.title,
                description = PreviewData.task.description,
                assignedToText = PreviewData.task.assignedTo.toString(),
                selectedPriority = "high",
                selectedStatus = "in_progress",
                dueDateText = PreviewData.task.dueDate
            ),
            onFormChange = {},
            onRetry = {},
            onSave = {},
            onBack = {}
        )
    }
}
