package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskComment
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.TasksApi
import com.example.teamworktracker.network.dto.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RemoteTaskRepository(
    private val api: TasksApi = ApiClient.retrofit().create(TasksApi::class.java)
) : TaskRepository {

    override suspend fun getMyTasks(): List<Task> {
        return api.getMyTasks().map { it.toDomain() }
    }

    override suspend fun getTaskById(taskId: Int): Task {
        return api.getTaskById(taskId).toDomain()
    }

    override suspend fun createTask(
        projectId: Int,
        assignedTo: Int,
        title: String,
        description: String,
        priority: String,
        status: String,
        dueDate: String
    ): Task {
        val dto = api.createTask(
            TaskCreateRequest(
                title = title,
                description = description,
                projectId = projectId,
                assignedTo = assignedTo,
                priority = priority,
                status = status,
                dueDate = dueDate
            )
        )
        return dto.toDomain()
    }

    override suspend fun updateTask(taskId: Int, body: Map<String, Any?>): Task {
        val req = TaskUpdateRequest(
            title = body["title"] as? String,
            description = body["description"] as? String,
            assignedTo = body["assigned_to"] as? Int,
            priority = body["priority"] as? String,
            status = body["status"] as? String,
            dueDate = body["due_date"] as? String
        )
        return api.updateTask(taskId, req).toDomain()
    }

    override suspend fun deleteTask(taskId: Int) {
        api.deleteTask(taskId)
    }

    // COMMENTS
    override suspend fun getTaskComments(taskId: Int): List<TaskComment> {
        return api.getComments(taskId).map { it.toDomain() }
    }

    override suspend fun addTaskComment(taskId: Int, content: String): TaskComment {
        return api.addComment(taskId, TaskCommentCreateRequest(content)).toDomain()
    }

    // ATTACHMENTS
    override suspend fun getTaskAttachments(taskId: Int): List<TaskAttachment> {
        return api.getAttachments(taskId).map { it.toDomain() }
    }

    override suspend fun addLinkAttachment(taskId: Int, url: String, description: String?): TaskAttachment {
        return api.addLinkAttachment(
            taskId,
            TaskAttachmentLinkCreateRequest(
                description = description?.takeIf { it.isNotBlank() },
                file_url = url
            )
        ).toDomain()
    }
}
