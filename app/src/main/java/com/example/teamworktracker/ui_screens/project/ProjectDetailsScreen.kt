package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.data.FakeProjectRepository
import com.example.teamworktracker.data.FakeTaskRepository
import com.example.teamworktracker.domain.models.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    projectId: Int,
    onBack: () -> Unit,
    onTaskClick: (Int) -> Unit
) {
    val project = FakeProjectRepository.getMyProjects().find { it.id == projectId }
    val allTasks = FakeTaskRepository.getMyTasks()
    val projectTasks: List<Task> = allTasks.filter { it.projectId == projectId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Project Details") }
            )
        }
    ) { padding ->
        if (project == null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Project not found", style = MaterialTheme.typography.titleMedium)
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // === Project info ===
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(project.name, style = MaterialTheme.typography.headlineSmall)
                    Text(project.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Project ID: ${project.id}")
                    Text("Team ID: ${project.teamId ?: "-"}")
                    Text("Status: ${project.status.name}")
                    Text("Created by (user id): ${project.createdBy}")
                    Text("Created at: ${project.createdAt}")
                    Text("Updated at: ${project.updatedAt}")
                }

                Divider()

                // === Project tasks ===
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tasks in this project",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (projectTasks.isEmpty()) {
                        Text(
                            text = "No tasks yet for this project.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        projectTasks.forEach { task ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(task.title, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        task.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Status: ${task.status.name}, Priority: ${task.priority.name}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Due: ${task.dueDate.take(10)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    TextButton(
                                        onClick = { onTaskClick(task.id) }
                                    ) {
                                        Text("Open Task")
                                    }
                                }
                            }
                        }
                    }
                }

                Divider()

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }
        }
    }
}
