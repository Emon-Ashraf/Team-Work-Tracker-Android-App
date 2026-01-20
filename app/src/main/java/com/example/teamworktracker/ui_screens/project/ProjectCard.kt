package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.domain.models.Project

@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {}
) {
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
            Text(project.name, style = MaterialTheme.typography.titleMedium)
            Text(project.description, style = MaterialTheme.typography.bodyMedium)

            val statusLabel = project.status.name.lowercase().replaceFirstChar { it.uppercase() }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("Status: $statusLabel") }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("Team: ${project.teamId ?: "-"}") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onViewDetails) {
                Text("View details")
            }
        }
    }
}
