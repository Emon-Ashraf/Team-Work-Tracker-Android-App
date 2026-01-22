package com.example.teamworktracker.network.dto

import com.google.gson.annotations.SerializedName

data class TaskCreateRequest(
    val title: String,
    val description: String,

    @SerializedName("project_id")
    val projectId: Int,

    @SerializedName("assigned_to")
    val assignedTo: Int,

    // send as String to match backend
    val priority: String,
    val status: String,

    @SerializedName("due_date")
    val dueDate: String
)
