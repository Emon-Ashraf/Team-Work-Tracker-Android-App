package com.example.teamworktracker.ui_screens.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Main sections


    //Team section
    object Teams : Screen("teams")
    object CreateTeam : Screen("create_team")
    object JoinTeam : Screen("join_team")
    object TeamDetails : Screen("team_details/{teamId}") {
        fun createRoute(teamId: Int) = "team_details/$teamId"
    }


    //Project part (✅ team-based)
    object Projects : Screen("projects/{teamId}") {
        fun createRoute(teamId: Int) = "projects/$teamId"
    }

    object CreateProject : Screen("createProject/{teamId}") {
        fun createRoute(teamId: Int) = "createProject/$teamId"
    }

    object ProjectDetails : Screen("projectDetails/{projectId}") {
        fun createRoute(projectId: Int) = "projectDetails/$projectId"
    }


    //Task section
    object MyTasks : Screen("my_tasks")
    object CreateTask : Screen("create_task")
    object TaskDetails : Screen("task_details/{taskId}") {
        fun createRoute(taskId: Int) = "task_details/$taskId"
    }
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
    object AddComment : Screen("add_comment/{taskId}") {
        fun createRoute(taskId: Int) = "add_comment/$taskId"
    }
    object AddAttachmentLink : Screen("add_attachment_link/{taskId}") {
        fun createRoute(taskId: Int) = "add_attachment_link/$taskId"
    }

    object AddAttachmentFile : Screen("add_attachment_file/{taskId}") {
        fun createRoute(taskId: Int) = "add_attachment_file/$taskId"
    }






}
