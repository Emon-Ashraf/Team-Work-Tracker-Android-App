package com.example.teamworktracker.ui_screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teamworktracker.R
import com.example.teamworktracker.data.testRepo.TestTaskRepository
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus
import com.example.teamworktracker.ui.theme.AppColors
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenMyTasks: () -> Unit,
    onOpenTeams: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onOpenMyTasks = onOpenMyTasks,
        onOpenTeams = onOpenTeams,
        onRefresh = { viewModel.loadDashboard() },
        onLoadDashboard = { viewModel.loadDashboard() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onOpenMyTasks: () -> Unit,
    onOpenTeams: () -> Unit,
    onRefresh: () -> Unit,
    onLoadDashboard: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoadDashboard()
    }

    Scaffold(
        containerColor = AppColors.BgDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Team Work Tracker") },
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
                        colors = listOf(
                            Color(0xFF0B0F2B),
                            AppColors.BgDark,
                            AppColors.BgDark
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Text(
                text = "Pick where you want to work:",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.MutedText
            )

            // 🔼 My Tasks FIRST
            DashboardTileCard(
                title = "My Tasks",
                subtitle = "View your daily todo list",
                iconRes = R.drawable.ic_my_tasks,
                isPrimary = true,
                onClick = onOpenMyTasks
            )

            // 🔼 Teams SECOND
            DashboardTileCard(
                title = "Teams",
                subtitle = "Open your teams and manage projects inside them.",
                iconRes = R.drawable.ic_teams,
                isPrimary = false,
                onClick = onOpenTeams
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 🔽 Today overview MOVED DOWN
            HomeOverviewCard(
                uiState = uiState,
                onRefresh = onRefresh,
                onOpenMyTasks = onOpenMyTasks
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tip: Projects belong to Teams. Open a team → Open Projects.",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.MutedText
            )
        }
    }
}

@Composable
private fun HomeOverviewCard(
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onOpenMyTasks: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardDark),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today overview",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onRefresh, enabled = !uiState.isLoading) {
                    Text("Refresh", color = AppColors.MutedText)
                }
            }

            when {
                uiState.isLoading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppColors.PrimaryBlue,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                else -> {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatChip(Modifier.weight(1f), "Total", uiState.totalTasks.toString())
                        StatChip(Modifier.weight(1f), "New", uiState.newCount.toString())
                        StatChip(Modifier.weight(1f), "In prog", uiState.inProgressCount.toString())
                        StatChip(Modifier.weight(1f), "High", uiState.highPriorityCount.toString())
                    }

                    Text(
                        text = "Next tasks",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall
                    )

                    if (uiState.nextTasksPreview.isEmpty()) {
                        Text(
                            text = "No tasks assigned to you yet.",
                            color = AppColors.MutedText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        uiState.nextTasksPreview.forEach {
                            TaskPreviewRow(task = it)
                        }
                    }

                    Button(
                        onClick = onOpenMyTasks,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryBlue),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Open My Tasks")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, color = AppColors.MutedText, style = MaterialTheme.typography.labelSmall)
        Text(
            value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TaskPreviewRow(task: Task) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                task.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val due = task.dueDate.take(10)
            Text(
                text = "Due: $due • ${statusLabel(task.status)} • ${priorityLabel(task.priority)}",
                color = AppColors.MutedText,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun statusLabel(s: TaskStatus): String = when (s) {
    TaskStatus.NEW -> "New"
    TaskStatus.IN_PROGRESS -> "In progress"
    TaskStatus.COMPLETED -> "Completed"
    TaskStatus.BLOCKED -> "Blocked"
}

private fun priorityLabel(p: TaskPriority): String = when (p) {
    TaskPriority.LOW -> "Low"
    TaskPriority.MEDIUM -> "Medium"
    TaskPriority.HIGH -> "High"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTileCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isPrimary) AppColors.PrimaryBlue else AppColors.CardDark
    val subtitleColor = if (isPrimary) Color.White.copy(alpha = 0.85f) else AppColors.MutedText
    val iconBg = if (isPrimary) Color.White.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.10f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPrimary) 10.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = subtitleColor)
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TeamWorkTrackerTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                tasks = TestTaskRepository.getMyTasks()
            ),
            onOpenMyTasks = {},
            onOpenTeams = {},
            onRefresh = {},
            onLoadDashboard = {}
        )
    }
}
