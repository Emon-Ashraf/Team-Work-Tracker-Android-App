package com.example.teamworktracker.ui_screens.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onProjectCreated: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var teamIdText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Project") }
            )
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
                label = { Text("Project name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = teamIdText,
                onValueChange = { teamIdText = it },
                label = { Text("Team ID (optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (name.isBlank()) {
                        error = "Project name is required"
                    } else {
                        error = null

                        // Later: POST /api/v1/projects/ with:
                        // {
                        //   "name": name,
                        //   "description": description,
                        //   "team_id": teamIdText.toIntOrNull()
                        // }
                        onProjectCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create")
            }

            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
