package com.example.teamworktracker.ui_screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teamworktracker.ui_screens.auth.LoginScreen
import com.example.teamworktracker.ui_screens.auth.RegisterScreen
import com.example.teamworktracker.ui_screens.home.HomeScreen
import com.example.teamworktracker.ui_screens.project.CreateProjectScreen
import com.example.teamworktracker.ui_screens.tasks.MyTasksScreen
import com.example.teamworktracker.ui_screens.project.ProjectsScreen
import com.example.teamworktracker.ui_screens.tasks.CreateTaskScreen
import com.example.teamworktracker.ui_screens.team.CreateTeamScreen
import com.example.teamworktracker.ui_screens.team.JoinTeamScreen
import com.example.teamworktracker.ui_screens.team.TeamsScreen

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.teamworktracker.ui_screens.project.ProjectDetailsScreen
import com.example.teamworktracker.ui_screens.tasks.AddCommentScreen
import com.example.teamworktracker.ui_screens.tasks.AddFileAttachmentScreen
import com.example.teamworktracker.ui_screens.tasks.AddLinkAttachmentScreen
import com.example.teamworktracker.ui_screens.tasks.TaskDetailsScreen

import com.example.teamworktracker.ui_screens.tasks.EditTaskScreen
import com.example.teamworktracker.ui_screens.team.TeamDetailsScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onOpenMyTasks = { navController.navigate(Screen.MyTasks.route) },
                onOpenProjects = { navController.navigate(Screen.Projects.route) },
                onOpenTeams = { navController.navigate(Screen.Teams.route) }
            )
        }



        //project section
        composable(Screen.Projects.route) {
            ProjectsScreen(
                onCreateProject = { navController.navigate(Screen.CreateProject.route) },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.createRoute(projectId))
                }
            )
        }


        composable(Screen.CreateProject.route) {
            CreateProjectScreen(
                onProjectCreated = {
                    navController.popBackStack() // back to Projects
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ProjectDetails.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
            ProjectDetailsScreen(
                projectId = projectId,
                onBack = { navController.popBackStack() },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetails.createRoute(taskId))
                }
            )
        }



        //Teams section
        composable(Screen.Teams.route) {
            TeamsScreen(
                onCreateTeam = { navController.navigate(Screen.CreateTeam.route) },
                onJoinTeam = { navController.navigate(Screen.JoinTeam.route) },
                onTeamClick = { teamId ->
                    navController.navigate(Screen.TeamDetails.createRoute(teamId))
                }
            )
        }


        composable(Screen.CreateTeam.route) {
            CreateTeamScreen(
                onTeamCreated = {
                    navController.popBackStack() // back to Teams
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.JoinTeam.route) {
            JoinTeamScreen(
                onJoined = {
                    navController.popBackStack() // back to Teams
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TeamDetails.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getInt("teamId") ?: -1
            TeamDetailsScreen(
                teamId = teamId,
                onBack = { navController.popBackStack() },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.createRoute(projectId))
                },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetails.createRoute(taskId))
                }
            )
        }


        //Task Section
        composable(Screen.MyTasks.route) {
            MyTasksScreen(
                onCreateTask = { navController.navigate(Screen.CreateTask.route) },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetails.createRoute(taskId))
                }
            )
        }



        composable(Screen.CreateTask.route) {
            CreateTaskScreen(
                onTaskCreated = {
                    navController.popBackStack() // back to MyTasks
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.TaskDetails.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            TaskDetailsScreen(
                taskId = taskId,
                onBack = { navController.popBackStack() },
                onEditTask = { id ->
                    navController.navigate(Screen.EditTask.createRoute(id))
                },
                onAddComment = { id ->
                    navController.navigate(Screen.AddComment.createRoute(id))
                },
                onAddAttachmentLink = { id ->
                    navController.navigate(Screen.AddAttachmentLink.createRoute(id))
                },
                onAddAttachmentFile = { id ->
                    navController.navigate(Screen.AddAttachmentFile.createRoute(id))
                }
            )
        }




        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            EditTaskScreen(
                taskId = taskId,
                onTaskUpdated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddComment.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            AddCommentScreen(
                taskId = taskId,
                onCommentAdded = {
                    navController.popBackStack()  // back to TaskDetails
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.AddAttachmentLink.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            AddLinkAttachmentScreen(
                taskId = taskId,
                onAttachmentAdded = {
                    navController.popBackStack() // back to TaskDetails
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.AddAttachmentFile.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            AddFileAttachmentScreen(
                taskId = taskId,
                onAttachmentAdded = {
                    navController.popBackStack() // back to TaskDetails
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }







    }
}
