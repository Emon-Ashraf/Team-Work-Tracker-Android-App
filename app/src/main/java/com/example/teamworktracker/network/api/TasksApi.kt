package com.example.teamworktracker.network.api

import com.example.teamworktracker.network.dto.TaskDto
import com.example.teamworktracker.network.dto.TaskCreateRequest
import com.example.teamworktracker.network.dto.TaskUpdateRequest
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
}
