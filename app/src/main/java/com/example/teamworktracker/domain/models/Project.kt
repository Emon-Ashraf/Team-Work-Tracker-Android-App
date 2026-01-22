package com.example.teamworktracker.domain.models

import com.google.gson.annotations.SerializedName

data class Project(
    val id: Int,
    val name: String,
    val description: String,

    @SerializedName("team_id")
    val teamId: Int,

    val status: ProjectStatus,

    @SerializedName("created_by")
    val createdBy: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)
