package com.example.teamworktracker.ui_screens.tasks

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
fun MyTasksScreen(
    onCreateTask: () -> Unit,
    onTaskClick: (Int) -> Unit,
    viewModel: MyTasksViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    // ✅ LOAD TASKS WHEN SCREEN OPENS
    LaunchedEffect(Unit) {
        viewModel.loadMyTasks()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("My Tasks") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(
                onClick = onCreateTask,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Create Task")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tasks assigned to you",
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

                uiState.tasks.isEmpty() -> {
                    Text(
                        text = "No tasks yet.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.tasks) { task ->
                            TaskCard(
                                task = task,
                                onViewDetails = { onTaskClick(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
