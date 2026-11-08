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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.R
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme
import com.example.teamworktracker.ui_screens.preview.PreviewData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    onTeamCreated: () -> Unit,
    onBack: () -> Unit,
    teamsViewModel: TeamsViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by teamsViewModel.state.collectAsState()

    CreateTeamScreenContent(
        uiState = uiState,
        name = name,
        onNameChange = { name = it },
        description = description,
        onDescriptionChange = { description = it },
        error = error,
        onErrorChange = { error = it },
        onCreateTeam = { name, description -> teamsViewModel.createTeam(name, description, onTeamCreated) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreenContent(
    uiState: TeamsUiState,
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    error: String?,
    onErrorChange: (String?) -> Unit,
    onCreateTeam: (String, String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            TopAppBar(
                title = { Text("Create Team") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Use your drawable icon
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
                    Box(
                        modifier = Modifier
                            .size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Team name
                Text("Team Name", color = Color.White, style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Marketing Squad", color = AppColors.MutedText) },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )
                Text(
                    text = "This will be the public name of your team.",
                    color = AppColors.MutedText,
                    style = MaterialTheme.typography.bodySmall
                )

                // Description
                Text("Description", color = Color.White, style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("What is this team working on?", color = AppColors.MutedText) },
                    enabled = !uiState.isLoading,
                    minLines = 4,
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Error handling (same logic)
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

            // Bottom buttons like sample (primary at bottom)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                Button(
                    onClick = {
                        if (name.isBlank()) {
                            onErrorChange("Name is required")
                        } else {
                            onErrorChange(null)
                            onCreateTeam(name.trim(), description.trim())
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
                    Text("Create Team")
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

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = AppColors.FieldBg,
    unfocusedContainerColor = AppColors.FieldBg,
    disabledContainerColor = AppColors.FieldBg.copy(alpha = 0.6f),

    focusedBorderColor = AppColors.PrimaryBlue,
    unfocusedBorderColor = AppColors.Border,
    disabledBorderColor = AppColors.Border.copy(alpha = 0.5f),

    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.7f),

    cursorColor = AppColors.PrimaryBlue
)

@Preview(name = "Light", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, widthDp = 390, heightDp = 844, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateTeamScreenPreview() {
    TeamWorkTrackerTheme(dynamicColor = false) {
        CreateTeamScreenContent(
            uiState = TeamsUiState(),
            name = PreviewData.team.name,
            onNameChange = {},
            description = PreviewData.team.description,
            onDescriptionChange = {},
            error = null,
            onErrorChange = {},
            onCreateTeam = { _, _ -> },
            onBack = {}
        )
    }
}
