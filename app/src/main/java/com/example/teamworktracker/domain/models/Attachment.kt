package com.example.teamworktracker.domain.models

data class Attachment(
    val id: Int,
    val taskId: Int,
    val userId: Int,
    val description: String,
    val filename: String?,
    val originalName: String?,
    val filePath: String?,
    val fileUrl: String?,
    val fileSize: Long?,
    val fileType: String?,
    val uploadedAt: String
)
