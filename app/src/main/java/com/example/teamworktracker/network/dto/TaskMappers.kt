package com.example.teamworktracker.network.dto

import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus

private fun String.toTaskPriority(): TaskPriority =
    runCatching { TaskPriority.valueOf(this.trim().uppercase()) }
        .getOrElse { TaskPriority.MEDIUM }

private fun String.toTaskStatus(): TaskStatus =
    runCatching { TaskStatus.valueOf(this.trim().uppercase()) }
        .getOrElse { TaskStatus.NEW }

// DTO -> Domain
fun TaskDto.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        projectId = projectId,
        assignedTo = assignedTo,
        priority = priority.toTaskPriority(),
        status = status.toTaskStatus(),
        dueDate = dueDate,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// Domain enum -> API string
// If your swagger shows enums are UPPERCASE, change `.lowercase()` to `.name`
fun TaskPriority.toApi(): String = name.lowercase()
fun TaskStatus.toApi(): String = name.lowercase()
