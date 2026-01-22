package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTeamScreen(
    onJoined: () -> Unit,
    onBack: () -> Unit,
    teamsViewModel: TeamsViewModel = viewModel() // ✅ ADD THIS
) {
    var teamIdText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by teamsViewModel.state.collectAsState() // ✅ ADD THIS

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Join Team") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = teamIdText,
                onValueChange = { teamIdText = it },
                label = { Text("Team ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    val id = teamIdText.toIntOrNull()
                    if (id == null) {
                        error = "Enter a valid numeric team ID"
                    } else {
                        error = null

                        // ✅ REAL API CALL HERE
                        teamsViewModel.joinTeam(
                            teamId = id,
                            onSuccess = onJoined
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Join")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
