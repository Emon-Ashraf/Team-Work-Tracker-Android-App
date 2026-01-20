package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    onCreateProject: () -> Unit,
    onProjectClick: (Int) -> Unit,
    viewModel: ProjectsViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Projects") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(
                onClick = onCreateProject,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Project")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "My Projects",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.myProjects.size) { index ->
                    val project = viewModel.myProjects[index]
                    ProjectCard(
                        project = project,
                        onViewDetails = { onProjectClick(project.id) }
                    )
                }
            }
        }
    }
}
