package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.data.testRepo.TestProjectRepository
import com.example.teamworktracker.data.testRepo.TestTaskRepository
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    projectId: Int,
    onBack: () -> Unit,
    onTaskClick: (Int) -> Unit
) {
    val project = TestProjectRepository.getMyProjects().find { it.id == projectId }
    val allTasks = TestTaskRepository.getMyTasks()
    val projectTasks: List<Task> = allTasks.filter { it.projectId == projectId }

    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Project Details") },
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
            .padding(16.dp)

        if (project == null) {
            Column(
                modifier = bg,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Project not found", style = MaterialTheme.typography.titleMedium, color = Color.White)

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue,
                        contentColor = Color.White
                    )
                ) { Text("Back") }
            }
        } else {
            Column(
                modifier = bg.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Project info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(project.name, style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text(project.description, style = MaterialTheme.typography.bodySmall, color = AppColors.MutedText)

                        Divider(color = AppColors.Border.copy(alpha = 0.6f))

                        InfoRow("Project ID", project.id.toString())
                        InfoRow("Team ID", project.teamId.toString())
                        InfoRow("Status", project.status.name)
                        InfoRow("Created by", project.createdBy.toString())
                        InfoRow("Created at", project.createdAt)
                        InfoRow("Updated at", project.updatedAt)
                    }
                }

                // Tasks section
                Text("Tasks in this project", style = MaterialTheme.typography.titleMedium, color = Color.White)

                if (projectTasks.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "No tasks yet for this project.",
                            color = AppColors.MutedText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        projectTasks.forEach { task ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(task.title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                                    Text(task.description, style = MaterialTheme.typography.bodySmall, color = AppColors.MutedText)

                                    Text(
                                        "Status: ${task.status.name} • Priority: ${task.priority.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppColors.MutedText
                                    )
                                    Text(
                                        "Due: ${task.dueDate.take(10)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AppColors.MutedText
                                    )

                                    TextButton(onClick = { onTaskClick(task.id) }) {
                                        Text("Open Task", color = AppColors.PrimaryBlue)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("›", color = AppColors.PrimaryBlue)
                                    }
                                }
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(AppColors.Border, AppColors.Border))
                    )
                ) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = AppColors.MutedText, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.width(10.dp))
        Text(value, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
    }
}
