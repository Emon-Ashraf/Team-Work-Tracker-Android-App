package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.data.FakeTaskRepository
import com.example.teamworktracker.data.FakeCommentRepository
import com.example.teamworktracker.data.FakeAttachmentRepository
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: Int,
    onBack: () -> Unit,
    onEditTask: (Int) -> Unit,
    onAddComment: (Int) -> Unit,
    onAddAttachmentLink: (Int) -> Unit,
    onAddAttachmentFile: (Int) -> Unit
) {
    val task = FakeTaskRepository.getTaskById(taskId)
    val comments = FakeCommentRepository.getCommentsForTask(taskId)
    val attachments = FakeAttachmentRepository.getAttachmentsForTask(taskId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Details") }
            )
        }
    ) { padding ->
        if (task == null) {
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
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),   // 👈 everything scrolls
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

                // === Comments section ===
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Comments",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (comments.isEmpty()) {
                        Text(
                            text = "No comments yet.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        comments.forEach { comment ->
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "User #${comment.userId} — ${comment.createdAt.take(10)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = comment.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }

                    Button(
                        onClick = { onAddComment(task.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Comment")
                    }
                }

                HorizontalDivider()

                // === Attachments section ===
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Attachments",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (attachments.isEmpty()) {
                        Text(
                            text = "No attachments yet.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        attachments.forEach { attachment ->
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                val typeLabel = when {
                                    attachment.fileType == "link" ||
                                            (attachment.fileUrl != null && attachment.filename == null) ->
                                        "Link"
                                    else -> "File"
                                }
                                Text(
                                    text = "$typeLabel — User #${attachment.userId} — ${attachment.uploadedAt.take(10)}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                val mainLine = attachment.description.ifBlank {
                                    attachment.originalName ?: attachment.fileUrl ?: "(no description)"
                                }
                                Text(
                                    text = mainLine,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (!attachment.fileUrl.isNullOrBlank()) {
                                    Text(
                                        text = attachment.fileUrl,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }

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

@Preview(showBackground = true)
@Composable
fun TaskDetailsScreenPreview() {
    TeamWorkTrackerTheme {
        TaskDetailsScreen(
            taskId = 1,
            onBack = {},
            onEditTask = {},
            onAddComment = {},
            onAddAttachmentLink = {},
            onAddAttachmentFile = {}
        )
    }
}