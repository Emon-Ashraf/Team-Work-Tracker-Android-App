package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.data.FakeTeamRepository
import com.example.teamworktracker.data.FakeProjectRepository
import com.example.teamworktracker.data.FakeTaskRepository
import com.example.teamworktracker.domain.models.Project
import com.example.teamworktracker.domain.models.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    teamId: Int,
    onBack: () -> Unit,
    onProjectClick: (Int) -> Unit,
    onTaskClick: (Int) -> Unit
) {
    val team = FakeTeamRepository.getMyTeams().find { it.id == teamId }

    val allProjects: List<Project> = FakeProjectRepository.getMyProjects()
    val teamProjects: List<Project> = allProjects.filter { it.teamId == teamId }

    val allTasks: List<Task> = FakeTaskRepository.getMyTasks()
    val projectIds = teamProjects.map { it.id }.toSet()
    val teamTasks: List<Task> = allTasks.filter { it.projectId in projectIds }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Team Details") }
            )
        }
    ) { padding ->
        if (team == null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Team not found", style = MaterialTheme.typography.titleMedium)
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

                // === Team info ===
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(team.name, style = MaterialTheme.typography.headlineSmall)
                    Text(team.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Team ID: ${team.id}")
                    Text("Created by (user id): ${team.createdBy}")
                    Text("Created at: ${team.createdAt}")
                    Text("Updated at: ${team.updatedAt}")
                }

                Divider()

                // === Team projects ===
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Projects in this team",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (teamProjects.isEmpty()) {
                        Text(
                            text = "No projects yet for this team.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        teamProjects.forEach { project ->
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
                                    Text(project.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        project.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Status: ${project.status.name}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    TextButton(
                                        onClick = { onProjectClick(project.id) }
                                    ) {
                                        Text("View Project")
                                    }
                                }
                            }
                        }
                    }
                }

                Divider()

                // === Team tasks (across all its projects) ===
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tasks in this team (all projects)",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (teamTasks.isEmpty()) {
                        Text(
                            text = "No tasks yet for this team.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        teamTasks.forEach { task ->
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
                                        "Project ID: ${task.projectId}, Status: ${task.status.name}, Priority: ${task.priority.name}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Due: ${task.dueDate.take(10)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    TextButton(onClick = { onTaskClick(task.id) }) {
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
