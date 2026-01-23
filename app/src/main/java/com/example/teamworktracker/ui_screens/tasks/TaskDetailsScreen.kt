package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus
import com.example.teamworktracker.ui.theme.AppColors

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

    // Load everything (task + comments + attachments)
    LaunchedEffect(taskId) {
        viewModel.loadAll(taskId)
    }

    // ✅ IMPORTANT: refresh when coming back from AddComment screen
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, taskId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadAll(taskId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back", color = AppColors.MutedText)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadAll(taskId) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete task", tint = Color(0xFFFF6B6B))
                    }
                },
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
                        onClick = { viewModel.loadAll(taskId) },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retry")
                    }
                }
            }

            uiState.task == null -> {
                Column(
                    modifier = bg.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Task not found", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back", color = AppColors.MutedText)
                    }
                }
            }

            else -> {
                val task = uiState.task!!

                Column(
                    modifier = bg
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ===== Header card =====
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = task.title,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                StatusPill(task.status)
                                PriorityPill(task.priority)
                            }

                            Text(
                                text = task.description,
                                color = AppColors.MutedText,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                            InfoRow(label = "Project ID", value = task.projectId.toString())
                            InfoRow(label = "Assigned to (user id)", value = task.assignedTo?.toString() ?: "-")
                            InfoRow(label = "Due date", value = task.dueDate)

                            Spacer(modifier = Modifier.height(2.dp))

                            InfoRow(label = "Created by", value = task.createdBy.toString())
                            InfoRow(label = "Created at", value = task.createdAt)
                            InfoRow(label = "Updated at", value = task.updatedAt)
                        }
                    }

                    Button(
                        onClick = { onEditTask(task.id) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                    ) {
                        Text("Edit Task")
                    }

                    // ===== Comments =====
                    SectionHeader(title = "Comments", rightHint = "${uiState.comments.size}")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (uiState.comments.isEmpty()) {
                                Text(
                                    text = "No comments yet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.MutedText
                                )
                            } else {
                                uiState.comments.forEach { c ->
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "User #${c.userId} • ${c.createdAt.take(10)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppColors.MutedText
                                        )
                                        Text(
                                            text = c.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                        HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                                    }
                                }
                            }

                            Button(
                                onClick = { onAddComment(task.id) },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.10f))
                            ) {
                                Text("Add Comment", color = Color.White)
                            }
                        }
                    }

                    // ===== Attachments =====
                    SectionHeader(title = "Attachments", rightHint = "${uiState.attachments.size}")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (uiState.attachments.isEmpty()) {
                                Text(
                                    text = "No attachments yet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.MutedText
                                )
                            } else {
                                uiState.attachments.forEach { a ->
                                    AttachmentRow(a)
                                    HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onAddAttachmentFile(task.id) },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue)
                                ) {
                                    Text("Add File")
                                }

                                OutlinedButton(
                                    onClick = { onAddAttachmentLink(task.id) },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                ) {
                                    Text("Add Link")
                                }
                            }
                        }
                    }

                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back", color = AppColors.MutedText)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun AttachmentRow(a: TaskAttachment) {
    val title = a.description?.takeIf { it.isNotBlank() }
        ?: a.originalName
        ?: a.filename
        ?: a.fileUrl
        ?: "(attachment)"

    Text(
        text = title,
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

    val meta = buildString {
        append("User #${a.userId}")
        append(" • ")
        append(a.uploadedAt.take(10))
        if (!a.fileType.isNullOrBlank()) {
            append(" • ")
            append(a.fileType)
        }
    }

    Text(
        text = meta,
        color = AppColors.MutedText,
        style = MaterialTheme.typography.labelSmall
    )

    if (!a.fileUrl.isNullOrBlank()) {
        Text(
            text = a.fileUrl,
            color = AppColors.MutedText,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SectionHeader(title: String, rightHint: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        if (rightHint != null) {
            Text(
                text = rightHint,
                color = AppColors.MutedText,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AppColors.MutedText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusPill(status: TaskStatus) {
    val (bg, fg) = when (status) {
        TaskStatus.NEW -> Color(0xFF1F2A44) to Color(0xFFAFC7FF)
        TaskStatus.IN_PROGRESS -> Color(0xFF2A2446) to Color(0xFFE3C7FF)
        TaskStatus.COMPLETED -> Color(0xFF143A2B) to Color(0xFF9CF2C8)
        TaskStatus.BLOCKED -> Color(0xFF3A1B1B) to Color(0xFFFFB3B3)
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = status.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PriorityPill(priority: TaskPriority) {
    val (bg, fg) = when (priority) {
        TaskPriority.HIGH -> Color(0xFF3A1B1B) to Color(0xFFFFB3B3)
        TaskPriority.MEDIUM -> Color(0xFF3A2E12) to Color(0xFFFFD79A)
        TaskPriority.LOW -> Color(0xFF143A2B) to Color(0xFF9CF2C8)
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}
