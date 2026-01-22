package com.example.teamworktracker.network.api

import com.example.teamworktracker.network.dto.TaskAttachmentLinkCreateRequest
import com.example.teamworktracker.network.dto.TaskAttachmentResponseDto
import com.example.teamworktracker.network.dto.TaskCommentCreateRequest
import com.example.teamworktracker.network.dto.TaskCommentResponseDto
import com.example.teamworktracker.network.dto.TaskDto
import com.example.teamworktracker.network.dto.TaskCreateRequest
import com.example.teamworktracker.network.dto.TaskUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface TasksApi {

    // GET /api/v1/tasks/my-tasks
    @GET("api/v1/tasks/my-tasks")
    suspend fun getMyTasks(): List<TaskDto>

    // GET /api/v1/tasks/{task_id}
    @GET("api/v1/tasks/{task_id}")
    suspend fun getTaskById(@Path("task_id") taskId: Int): TaskDto

    // POST /api/v1/tasks/
    @POST("api/v1/tasks/")
    suspend fun createTask(@Body body: TaskCreateRequest): TaskDto

    // PUT /api/v1/tasks/{task_id}
    @PUT("api/v1/tasks/{task_id}")
    suspend fun updateTask(
        @Path("task_id") taskId: Int,
        @Body body: TaskUpdateRequest
    ): TaskDto

    // DELETE /api/v1/tasks/{task_id}
    @DELETE("api/v1/tasks/{task_id}")
    suspend fun deleteTask(@Path("task_id") taskId: Int)

    // ✅ COMMENTS
    @POST("/api/v1/tasks/{taskId}/comments")
    suspend fun addComment(
        @Path("taskId") taskId: Int,
        @Body body: TaskCommentCreateRequest
    ): TaskCommentResponseDto

    @GET("/api/v1/tasks/{taskId}/comments")
    suspend fun getComments(
        @Path("taskId") taskId: Int
    ): List<TaskCommentResponseDto>

    // ✅ ATTACHMENTS (LINK)
    @POST("/api/v1/tasks/{taskId}/attachments/link")
    suspend fun addLinkAttachment(
        @Path("taskId") taskId: Int,
        @Body body: TaskAttachmentLinkCreateRequest
    ): TaskAttachmentResponseDto

    @GET("/api/v1/tasks/{taskId}/attachments")
    suspend fun getAttachments(
        @Path("taskId") taskId: Int
    ): List<TaskAttachmentResponseDto>

    // ✅ ATTACHMENTS (FILE UPLOAD)
    @Multipart
    @POST("/api/v1/tasks/{taskId}/attachments/file")
    suspend fun uploadFileAttachment(
        @Path("taskId") taskId: Int,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?
    ): TaskAttachmentResponseDto
}
