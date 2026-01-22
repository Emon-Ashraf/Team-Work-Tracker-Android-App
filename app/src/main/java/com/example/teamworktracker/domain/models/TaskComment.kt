package com.example.teamworktracker.domain.models

data class TaskComment(
    val id: Int,
    val taskId: Int,
    val userId: Int,
    val content: String,
    val createdAt: String,
    val updatedAt: String?
)
