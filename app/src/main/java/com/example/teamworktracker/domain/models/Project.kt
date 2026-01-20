package com.example.teamworktracker.domain.models

data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val teamId: Int?,
    val status: ProjectStatus,
    val createdBy: Int,
    val createdAt: String,
    val updatedAt: String
)
