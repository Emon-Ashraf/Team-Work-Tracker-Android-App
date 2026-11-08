package com.example.teamworktracker.ui_screens.team

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.R
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTeamScreen(
    onJoined: () -> Unit,
    onBack: () -> Unit,
    teamsViewModel: TeamsViewModel = viewModel()
) {
    var teamIdText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by teamsViewModel.state.collectAsState()

    JoinTeamScreenContent(
        uiState = uiState,
        teamIdText = teamIdText,
        onTeamIdTextChange = { teamIdText = it },
        error = error,
        onErrorChange = { error = it },
        onJoinTeam = { teamsViewModel.joinTeam(it, onJoined) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTeamScreenContent(
    uiState: TeamsUiState,
    teamIdText: String,
    onTeamIdTextChange: (String) -> Unit,
    error: String?,
    onErrorChange: (String?) -> Unit,
    onJoinTeam: (Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            TopAppBar(
                title = { Text("Join Team") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BgDark,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0B0F2B), AppColors.BgDark, AppColors.BgDark)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                // Optional top icon (like sample)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_join),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Text(
                    text = "Enter the team ID provided by your team admin to join the workspace.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.MutedText
                )

                Text("Team ID", color = Color.White, style = MaterialTheme.typography.labelLarge)

                OutlinedTextField(
                    value = teamIdText,
                    onValueChange = onTeamIdTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("e.g. 123456", color = AppColors.MutedText) },
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                val shownError = error ?: uiState.error
                if (shownError != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = shownError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val id = teamIdText.toIntOrNull()
                        if (id == null) {
                            onErrorChange("Enter a valid numeric team ID")
                        } else {
                            onErrorChange(null)
                            onJoinTeam(id)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text("Join")
                }

                TextButton(
                    onClick = onBack,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = AppColors.MutedText)
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun JoinTeamScreenPreview() {
    TeamWorkTrackerTheme(dynamicColor = false) {
        JoinTeamScreenContent(
            uiState = TeamsUiState(),
            teamIdText = "1",
            onTeamIdTextChange = {},
            error = null,
            onErrorChange = {},
            onJoinTeam = {},
            onBack = {}
        )
    }
}
