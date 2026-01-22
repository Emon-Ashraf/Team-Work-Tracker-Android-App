package com.example.teamworktracker.domain.models

data class TaskAttachment(
    val id: Int,
    val taskId: Int,
    val userId: Int,

    val description: String?,     //  nullable (Swagger allows)
    val filename: String?,
    val originalName: String?,
    val filePath: String?,        //  add because your model expects it
    val fileUrl: String?,
    val fileSize: Long?,           //  add because your model expects it
    val fileType: String?,
    val uploadedAt: String
)
