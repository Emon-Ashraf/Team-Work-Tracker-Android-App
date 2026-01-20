package com.example.teamworktracker.domain.models

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val projectId: Int,
    val assignedTo: Int?,
    val priority: TaskPriority,
    val status: TaskStatus,
    val dueDate: String,
    val createdBy: Int,
    val createdAt: String,
    val updatedAt: String
)
