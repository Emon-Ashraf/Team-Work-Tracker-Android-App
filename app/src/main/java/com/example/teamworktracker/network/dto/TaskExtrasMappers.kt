package com.example.teamworktracker.network.dto

import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskComment

fun TaskCommentResponseDto.toDomain(): TaskComment = TaskComment(
    id = id,
    taskId = task_id,
    userId = user_id,
    content = content,
    createdAt = created_at,
    updatedAt = updated_at
)

fun TaskAttachmentResponseDto.toDomain(): TaskAttachment = TaskAttachment(
    id = id,
    taskId = task_id,
    userId = user_id,

    description = description,     // String? now matches domain
    filename = filename,
    originalName = original_name,
    filePath = file_path,          //  PASS IT
    fileUrl = file_url,
    fileSize = file_size?.toLong(),          //  PASS IT
    fileType = file_type,
    uploadedAt = uploaded_at
)
