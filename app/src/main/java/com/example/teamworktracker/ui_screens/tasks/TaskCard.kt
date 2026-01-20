package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.domain.models.Task

@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {}
) {
    val statusLabel = task.status.name.lowercase().replaceFirstChar { it.uppercase() }
    val priorityLabel = task.priority.name.lowercase().replaceFirstChar { it.uppercase() }
    val dueDateShort = task.dueDate.take(10)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            Text(task.description, style = MaterialTheme.typography.bodyMedium)
            Text("Project ID: ${task.projectId}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(onClick = {}, label = { Text("Status: $statusLabel") })
                AssistChip(onClick = {}, label = { Text("Priority: $priorityLabel") })
                AssistChip(onClick = {}, label = { Text("Due: $dueDateShort") })
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 👇 Explicit button instead of card click
            TextButton(onClick = onViewDetails) {
                Text("View details")
            }
        }
    }
}

