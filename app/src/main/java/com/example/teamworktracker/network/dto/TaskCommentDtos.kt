package com.example.teamworktracker.network.dto

data class TaskCommentCreateRequest(
    val content: String
)

data class TaskCommentResponseDto(
    val content: String,
    val id: Int,
    val task_id: Int,
    val user_id: Int,
    val created_at: String,
    val updated_at: String?
)
