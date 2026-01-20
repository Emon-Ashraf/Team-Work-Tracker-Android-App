package com.example.teamworktracker.ui_screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Task")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tasks assigned to you",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.tasks.size) { index ->
                    val task = viewModel.tasks[index]
                    TaskCard(
                        task = task,
                        onViewDetails = { onTaskClick(task.id) }
                    )
                }
            }


        }
    }
}
