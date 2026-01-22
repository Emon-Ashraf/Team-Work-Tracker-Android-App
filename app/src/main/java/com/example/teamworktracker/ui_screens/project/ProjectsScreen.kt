package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    teamId: Int,                        // ✅ ADD THIS
    onCreateProject: () -> Unit,
    onProjectClick: (Int) -> Unit,
    viewModel: ProjectsViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    // ✅ LOAD PROJECTS FOR THIS TEAM
    LaunchedEffect(teamId) {
        viewModel.loadProjectsForTeam(teamId)
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Projects") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(
                onClick = onCreateProject,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Create Project")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Projects in this team",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.projects.isEmpty() -> {
                    Text(
                        text = "No projects yet for this team.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.projects) { project ->
                            ProjectCard(
                                project = project,
                                onViewDetails = { onProjectClick(project.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
