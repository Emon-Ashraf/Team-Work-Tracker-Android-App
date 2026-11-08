package com.example.teamworktracker.ui_screens.preview

import com.example.teamworktracker.domain.models.Project
import com.example.teamworktracker.domain.models.ProjectStatus
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskComment
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus
import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember

/** Deterministic, dependency-free fixtures used only by Compose previews. */
internal object PreviewData {
    private const val createdAt = "2026-11-02T09:00:00Z"
    private const val updatedAt = "2026-11-08T14:30:00Z"

    val team = Team(
        id = 1,
        name = "Mobile Product Team",
        description = "Building a better way to collaborate and track project delivery.",
        createdBy = 7,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    val teams = listOf(team, team.copy(id = 2, name = "Design Studio", description = "Product research and interface design."))
    val members = listOf(
        TeamMember(id = 1, teamId = team.id, userId = 7, joinedAt = createdAt),
        TeamMember(id = 2, teamId = team.id, userId = 12, joinedAt = updatedAt)
    )
    val project = Project(
        id = 1,
        name = "TeamWorkTracker Mobile",
        description = "Deliver the next version of our team's project management app.",
        teamId = team.id,
        status = ProjectStatus.IN_PROGRESS,
        createdBy = 7,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    val projects = listOf(project, project.copy(id = 2, name = "Website Refresh", status = ProjectStatus.PLANNING))
    val task = Task(
        id = 1,
        title = "Polish the project dashboard",
        description = "Review task summaries and prepare the dashboard for the next product demo.",
        projectId = project.id,
        assignedTo = 12,
        priority = TaskPriority.HIGH,
        status = TaskStatus.IN_PROGRESS,
        dueDate = "2026-11-15T17:00:00Z",
        createdBy = 7,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    val tasks = listOf(
        task,
        task.copy(id = 2, title = "Review the release checklist", priority = TaskPriority.MEDIUM, status = TaskStatus.NEW),
        task.copy(id = 3, title = "Share the design handoff", priority = TaskPriority.LOW, status = TaskStatus.COMPLETED)
    )
    val comments = listOf(
        TaskComment(
            id = 1, taskId = task.id, userId = 7,
            content = "The updated layout is ready for review. Please check the task summary cards.",
            createdAt = updatedAt, updatedAt = null
        )
    )
    val attachments = listOf(
        TaskAttachment(
            id = 1, taskId = task.id, userId = 12,
            description = "Dashboard design specification",
            filename = "dashboard-spec.pdf", originalName = "dashboard-spec.pdf",
            filePath = null, fileUrl = "https://example.com/dashboard-spec.pdf",
            fileSize = 245_760L, fileType = "application/pdf", uploadedAt = updatedAt
        )
    )
}
