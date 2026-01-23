package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.R
import com.example.teamworktracker.domain.models.Project
import com.example.teamworktracker.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {}
) {
    val statusLabel = project.status.name.lowercase().replaceFirstChar { it.uppercase() }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onViewDetails,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_projects), // <-- add icon
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.MutedText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = { Text("Status: $statusLabel") },
                    colors = AssistChipDefaults.assistChipColors(
                        disabledContainerColor = Color.White.copy(alpha = 0.08f),
                        disabledLabelColor = AppColors.MutedText
                    )
                )
                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = { Text("Team: ${project.teamId}") },
                    colors = AssistChipDefaults.assistChipColors(
                        disabledContainerColor = Color.White.copy(alpha = 0.08f),
                        disabledLabelColor = AppColors.MutedText
                    )
                )
            }


        }
    }
}
