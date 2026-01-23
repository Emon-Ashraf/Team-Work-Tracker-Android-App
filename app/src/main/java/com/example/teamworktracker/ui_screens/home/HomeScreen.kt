package com.example.teamworktracker.ui_screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.teamworktracker.R
import com.example.teamworktracker.ui.theme.AppColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenMyTasks: () -> Unit,
    onOpenTeams: () -> Unit
) {
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

            Spacer(modifier = Modifier.height(4.dp))

            // My Tasks
            DashboardTileCard(
                title = "My Tasks",
                subtitle = "View your daily todo list",
                iconRes = R.drawable.ic_my_tasks, // <-- your drawable
                isPrimary = true,
                onClick = onOpenMyTasks
            )

            // Teams
            DashboardTileCard(
                title = "Teams",
                subtitle = "Open your teams and manage projects inside them.",
                iconRes = R.drawable.ic_teams, // <-- your drawable
                isPrimary = false,
                onClick = onOpenTeams
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
        onClick = onClick, // avoids Modifier.clickable crash
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = subtitleColor
                )
            }

            // Chevron without material icons dependency
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}
