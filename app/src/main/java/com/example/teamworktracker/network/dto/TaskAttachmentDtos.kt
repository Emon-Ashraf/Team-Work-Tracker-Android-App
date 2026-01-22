package com.example.teamworktracker.network.dto

data class TaskAttachmentLinkCreateRequest(
    val description: String?,
    val file_url: String
)

data class TaskAttachmentResponseDto(
    val description: String?,
    val id: Int,
    val task_id: Int,
    val user_id: Int,
    val filename: String?,
    val original_name: String?,
    val file_path: String?,
    val file_url: String?,
    val file_size: Int?,
    val file_type: String?,
    val uploaded_at: String
)
