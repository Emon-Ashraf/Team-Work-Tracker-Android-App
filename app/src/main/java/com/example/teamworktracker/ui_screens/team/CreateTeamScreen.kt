package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    onTeamCreated: () -> Unit,
    onBack: () -> Unit,
    teamsViewModel: TeamsViewModel = viewModel() // ✅ ADD THIS
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by teamsViewModel.state.collectAsState() // ✅ ADD THIS

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Create Team") })
        }
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
                label = { Text("Team name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Show error from screen validation OR API
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
                        error = "Name is required"
                    } else {
                        error = null

                        // ✅ HERE is the REAL API CALL
                        teamsViewModel.createTeam(
                            name = name.trim(),
                            description = description.trim(),
                            onSuccess = onTeamCreated
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
