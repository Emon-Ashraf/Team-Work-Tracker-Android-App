package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    teamId: Int, // ✅ passed from navigation / TeamDetails
    onProjectCreated: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProjectsViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Create Project") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Project name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            val shownError = error ?: uiState.error
            if (shownError != null) {
                Text(text = shownError, color = MaterialTheme.colorScheme.error)
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            Button(
                onClick = {
                    if (name.isBlank()) {
                        error = "Project name is required"
                    } else {
                        error = null

                        // ✅ PASTE IT HERE (REAL API CALL)
                        viewModel.createProject(
                            teamId = teamId,
                            name = name.trim(),
                            description = description.trim(),
                            onSuccess = onProjectCreated
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Create")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
