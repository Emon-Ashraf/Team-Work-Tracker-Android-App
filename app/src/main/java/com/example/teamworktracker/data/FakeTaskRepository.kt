package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.*

object FakeTaskRepository {

    // Simulates GET /api/v1/tasks/my-tasks
    fun getMyTasks(): List<Task> {
        return listOf(
            Task(
                id = 1,
                title = "Design Login Screen UI",
                description = "Create clean login screen with email and password.",
                projectId = 1,
                assignedTo = 10,
                priority = TaskPriority.HIGH,
                status = TaskStatus.IN_PROGRESS,
                dueDate = "2025-01-20T10:00:00Z",
                createdBy = 5,
                createdAt = "2025-01-10T09:00:00Z",
                updatedAt = "2025-01-12T12:00:00Z"
            ),
            Task(
                id = 2,
                title = "Implement Team List",
                description = "Show list of teams with create/join buttons.",
                projectId = 1,
                assignedTo = 10,
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.NEW,
                dueDate = "2025-01-25T18:00:00Z",
                createdBy = 5,
                createdAt = "2025-01-11T14:30:00Z",
                updatedAt = "2025-01-11T14:30:00Z"
            ),
            Task(
                id = 3,
                title = "Update Project Repository",
                description = "Align project model with backend API.",
                projectId = 2,
                assignedTo = 10,
                priority = TaskPriority.LOW,
                status = TaskStatus.COMPLETED,
                dueDate = "2025-01-15T12:00:00Z",
                createdBy = 6,
                createdAt = "2025-01-09T16:00:00Z",
                updatedAt = "2025-01-15T11:00:00Z"
            )
        )
    }

    // 👇 ADD IT RIGHT HERE
    fun getTaskById(id: Int): Task? {
        return getMyTasks().find { it.id == id }
    }
}
