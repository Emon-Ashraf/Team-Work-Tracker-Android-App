package com.example.teamworktracker.ui_screens.project

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme
import com.example.teamworktracker.ui_screens.preview.PreviewData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    teamId: Int,
    onProjectCreated: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProjectsViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    CreateProjectScreenContent(
        uiState = uiState,
        name = name,
        onNameChange = { name = it },
        description = description,
        onDescriptionChange = { description = it },
        error = error,
        onErrorChange = { error = it },
        onCreateProject = { name, description -> viewModel.createProject(teamId, name, description, onProjectCreated) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreenContent(
    uiState: ProjectsUiState,
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    error: String?,
    onErrorChange: (String?) -> Unit,
    onCreateProject: (String, String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Project") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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

                Text("Project Name", color = Color.White, style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    placeholder = { Text("e.g. Mobile App Revamp", color = AppColors.MutedText) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    colors = fieldColors(),
                    shape = RoundedCornerShape(14.dp)
                )

                Text("Description", color = Color.White, style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    minLines = 4,
                    placeholder = { Text("What is this project about?", color = AppColors.MutedText) },
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
                        if (name.isBlank()) {
                            onErrorChange("Project name is required")
                        } else {
                            onErrorChange(null)
                            onCreateProject(name.trim(), description.trim())
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
                    Text("Create")
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
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
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
private fun CreateProjectScreenPreview() {
    TeamWorkTrackerTheme(dynamicColor = false) {
        CreateProjectScreenContent(
            uiState = ProjectsUiState(),
            name = PreviewData.project.name,
            onNameChange = {},
            description = PreviewData.project.description,
            onDescriptionChange = {},
            error = null,
            onErrorChange = {},
            onCreateProject = { _, _ -> },
            onBack = {}
        )
    }
}
