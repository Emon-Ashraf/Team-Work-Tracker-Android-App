package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskComment

interface TaskRepository {
    suspend fun getMyTasks(): List<Task>
    suspend fun getTaskById(taskId: Int): Task
    suspend fun createTask(
        projectId: Int,
        assignedTo: Int,
        title: String,
        description: String,
        priority: String,
        status: String,
        dueDate: String
    ): Task

    suspend fun updateTask(taskId: Int, body: Map<String, Any?>): Task
    suspend fun deleteTask(taskId: Int)

    // Comments
    suspend fun getTaskComments(taskId: Int): List<TaskComment>
    suspend fun addTaskComment(taskId: Int, content: String): TaskComment

    // Attachments
    suspend fun getTaskAttachments(taskId: Int): List<TaskAttachment>
    suspend fun addLinkAttachment(taskId: Int, url: String, description: String?): TaskAttachment


}
