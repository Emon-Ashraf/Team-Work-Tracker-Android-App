package com.example.teamworktracker.ui_screens.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    onCreateTeam: () -> Unit,
    onJoinTeam: () -> Unit,
    onTeamClick: (Int) -> Unit,
    viewModel: TeamsViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyTeams()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Teams") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCreateTeam,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Create Team")
                }
                OutlinedButton(
                    onClick = onJoinTeam,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Join Team")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "My Teams",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.teams) { team ->
                        TeamCard(
                            team = team,
                            onViewDetails = { onTeamClick(team.id) }
                        )
                    }
                }
            }
        }
    }
}
