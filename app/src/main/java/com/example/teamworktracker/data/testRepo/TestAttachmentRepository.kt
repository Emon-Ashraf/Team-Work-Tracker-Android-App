package com.example.teamworktracker.data.testRepo

import com.example.teamworktracker.domain.models.TaskAttachment

object TestAttachmentRepository {

    // Simulate GET /api/v1/tasks/{task_id}/attachments
    fun getAttachmentsForTask(taskId: Int): List<TaskAttachment> {
        return listOf(
            TaskAttachment(
                id = 1,
                taskId = taskId,
                userId = 10,
                description = "UI mockups (Figma)",
                filename = null,
                originalName = null,
                filePath = null,
                fileUrl = "https://figma.com/file/example",
                fileSize = null,
                fileType = "link",
                uploadedAt = "2025-01-10T10:00:00Z"
            ),
            TaskAttachment(
                id = 2,
                taskId = taskId,
                userId = 11,
                description = "API design doc",
                filename = "api_design_v1.pdf",
                originalName = "api_design_v1.pdf",
                filePath = "/files/tasks/2/api_design_v1.pdf",
                fileUrl = "https://files.example.com/api_design_v1.pdf",
                fileSize = 1024 * 200L,
                fileType = "application/pdf",
                uploadedAt = "2025-01-11T12:30:00Z"
            )
        )
    }
}
