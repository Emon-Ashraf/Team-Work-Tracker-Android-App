package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    teamId: Int,
    onBack: () -> Unit,
    onProjectClick: (Int) -> Unit, // keep for later
    onTaskClick: (Int) -> Unit,    // keep for later
    viewModel: TeamDetailsViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    // ✅ REAL API LOAD HERE
    LaunchedEffect(teamId) {
        viewModel.load(teamId)
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Team Details") }) }
    ) { padding ->

        when {
            uiState.loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) { CircularProgressIndicator() }
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
                    TextButton(onClick = onBack) { Text("Back") }
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
                    TextButton(onClick = onBack) { Text("Back") }
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
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(team.name, style = MaterialTheme.typography.headlineSmall)
                        Text(team.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Team ID: ${team.id}")
                        Text("Created by: ${team.createdBy}")
                        Text("Created at: ${team.createdAt}")
                        Text("Updated at: ${team.updatedAt}")
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
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("User ID: ${m.userId}", style = MaterialTheme.typography.titleSmall)
                                    Text("Joined: ${m.joinedAt}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }

                    Divider()

                    // Projects/tasks area will be wired when we implement Projects/Tasks APIs.
                    Text(
                        "Projects and tasks will appear here after Projects API integration.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("Back")
                    }
                }
            }
        }
    }
}
