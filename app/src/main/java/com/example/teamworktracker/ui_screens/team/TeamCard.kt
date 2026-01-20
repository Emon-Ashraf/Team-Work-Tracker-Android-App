package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.domain.models.Team

@Composable
fun TeamCard(
    team: Team,
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
            Text(team.name, style = MaterialTheme.typography.titleMedium)
            Text(team.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Team ID: ${team.id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Created by (user id): ${team.createdBy}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onViewDetails) {
                Text("View details")
            }
        }
    }
}
