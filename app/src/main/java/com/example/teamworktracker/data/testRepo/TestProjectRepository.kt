package com.example.teamworktracker.data.testRepo

import com.example.teamworktracker.domain.models.Project
import com.example.teamworktracker.domain.models.ProjectStatus

object TestProjectRepository {

    fun getMyProjects(): List<Project> {
        // shape similar to GET /api/v1/projects/
        return listOf(
            Project(
                id = 1,
                name = "Team Work Tracker",
                description = "Main app for tracking teams, projects and tasks.",
                teamId = 1,
                status = ProjectStatus.PLANNING,
                createdBy = 1,
                createdAt = "2025-01-05T10:00:00Z",
                updatedAt = "2025-01-10T12:00:00Z"
            ),
            Project(
                id = 2,
                name = "AgroLink",
                description = "Farmer to buyer bridge MVP.",
                teamId = 2,
                status = ProjectStatus.IN_PROGRESS,
                createdBy = 2,
                createdAt = "2025-01-08T09:30:00Z",
                updatedAt = "2025-01-12T16:45:00Z"
            )
        )
    }
}
