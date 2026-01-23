package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus
import com.example.teamworktracker.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {}
) {
    val dueDateShort = task.dueDate.take(10)

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onViewDetails,
        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top row: Status + Priority (bigger pills)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(status = task.status)
                PriorityPill(priority = task.priority)
                Spacer(modifier = Modifier.weight(1f))

                // small "chevron" to hint navigation, no icon dependency
                Text(
                    text = "›",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }

            // Title
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Description (show more)
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.MutedText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Bottom info row: Due + Project ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoChip(text = "Due: $dueDateShort")
                InfoChip(text = "Project: ${task.projectId}")
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = AppColors.MutedText
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
            color = fg
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
            color = fg
        )
    }
}
