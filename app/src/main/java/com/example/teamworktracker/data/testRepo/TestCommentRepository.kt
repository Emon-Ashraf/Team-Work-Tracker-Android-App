package com.example.teamworktracker.data.testRepo

import com.example.teamworktracker.domain.models.TaskComment

object TestCommentRepository {

    fun getCommentsForTask(taskId: Int): List<TaskComment> {
        // Simulate GET /api/v1/tasks/{task_id}/comments
        return listOf(
            TaskComment(
                id = 1,
                taskId = taskId,
                userId = 10,
                content = "Initial task created. Please check the UI layout.",
                createdAt = "2025-01-10T09:10:00Z",
                updatedAt = "2025-01-10T09:10:00Z"
            ),
            TaskComment(
                id = 2,
                taskId = taskId,
                userId = 11,
                content = "I pushed some changes to the login screen.",
                createdAt = "2025-01-11T15:25:00Z",
                updatedAt = "2025-01-11T15:25:00Z"
            ),
            TaskComment(
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
