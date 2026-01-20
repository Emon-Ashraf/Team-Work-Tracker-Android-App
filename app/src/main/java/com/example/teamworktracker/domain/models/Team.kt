package com.example.teamworktracker.domain.models

data class Team(
    val id: Int,
    val name: String,
    val description: String,
    val createdBy: Int,
    val createdAt: String,
    val updatedAt: String
)
