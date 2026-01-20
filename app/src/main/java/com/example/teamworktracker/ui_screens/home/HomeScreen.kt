package com.example.teamworktracker.ui_screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenMyTasks: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenTeams: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Team Work Tracker") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Choose where you want to work:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // My Tasks
            Button(
                onClick = onOpenMyTasks,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Tasks")
            }

            // Projects
            OutlinedButton(
                onClick = onOpenProjects,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Projects")
            }

            // Teams
            OutlinedButton(
                onClick = onOpenTeams,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Teams")
            }

            // Later we can add quick stats here:
            // - tasks due today
            // - active sprints, etc.
        }
    }
}
