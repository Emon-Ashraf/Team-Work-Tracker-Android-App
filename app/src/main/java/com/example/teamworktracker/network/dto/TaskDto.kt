package com.example.teamworktracker.network.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    val id: Int,
    val title: String,
    val description: String,

    @SerializedName("project_id")
    val projectId: Int,

    @SerializedName("assigned_to")
    val assignedTo: Int,

    // keep as String to avoid enum mismatch issues
    val priority: String,
    val status: String,

    @SerializedName("due_date")
    val dueDate: String,

    @SerializedName("created_by")
    val createdBy: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)
