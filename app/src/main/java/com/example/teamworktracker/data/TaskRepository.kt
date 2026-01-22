package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Task

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
}
