package com.example.teamworktracker.network.dto

import com.google.gson.annotations.SerializedName

data class TaskUpdateRequest(
    val title: String? = null,
    val description: String? = null,

    @SerializedName("assigned_to")
    val assignedTo: Int? = null,

    val priority: String? = null,
    val status: String? = null,

    @SerializedName("due_date")
    val dueDate: String? = null
)
