package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    teamId: Int,
    onBack: () -> Unit,

    // ✅ This should navigate to Projects list for this team:
    // navController.navigate(Screen.Projects.createRoute(teamId))
    onOpenProjects: (Int) -> Unit,

    // ✅ Keep for later (task list / task details from this team)
    onTaskClick: (Int) -> Unit,

    viewModel: TeamDetailsViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.load(teamId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Team Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        when {
            uiState.loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back")
                    }
                }
            }

            uiState.team == null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Team not found", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back")
                    }
                }
            }

            else -> {
                val team = uiState.team!!

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // === Team info ===
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(team.name, style = MaterialTheme.typography.headlineSmall)
                            Text(team.description, style = MaterialTheme.typography.bodyMedium)

                            Divider(modifier = Modifier.padding(vertical = 4.dp))

                            Text("Team ID: ${team.id}")
                            Text("Created by: ${team.createdBy}")
                            Text("Created at: ${team.createdAt}")
                            Text("Updated at: ${team.updatedAt}")
                        }
                    }

                    // ✅ Main action for this screen
                    Button(
                        onClick = { onOpenProjects(team.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open Projects")
                    }

                    Divider()

                    // === Members ===
                    Text("Members", style = MaterialTheme.typography.titleMedium)

                    if (uiState.members.isEmpty()) {
                        Text("No members found.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        uiState.members.forEach { m ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("User ID: ${m.userId}", style = MaterialTheme.typography.titleSmall)
                                    Text("Joined: ${m.joinedAt}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }

                    Divider()

                    // Optional placeholder (until you wire tasks by team)
                    Text(
                        text = "Tasks for this team will appear here after Tasks API integration.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back")
                    }
                }
            }
        }
    }
}
