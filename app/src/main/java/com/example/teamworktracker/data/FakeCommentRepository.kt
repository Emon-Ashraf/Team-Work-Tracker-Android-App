package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Comment

object FakeCommentRepository {

    fun getCommentsForTask(taskId: Int): List<Comment> {
        // Simulate GET /api/v1/tasks/{task_id}/comments
        return listOf(
            Comment(
                id = 1,
                taskId = taskId,
                userId = 10,
                content = "Initial task created. Please check the UI layout.",
                createdAt = "2025-01-10T09:10:00Z",
                updatedAt = "2025-01-10T09:10:00Z"
            ),
            Comment(
                id = 2,
                taskId = taskId,
                userId = 11,
                content = "I pushed some changes to the login screen.",
                createdAt = "2025-01-11T15:25:00Z",
                updatedAt = "2025-01-11T15:25:00Z"
            ),
            Comment(
                id = 3,
                taskId = taskId,
                userId = 10,
                content = "Looks good. Next step: connect to backend.",
                createdAt = "2025-01-12T11:40:00Z",
                updatedAt = "2025-01-12T11:40:00Z"
            )
        )
    }
}
