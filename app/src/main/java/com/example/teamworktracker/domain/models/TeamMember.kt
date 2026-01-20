package com.example.teamworktracker.domain.models

data class TeamMember(
    val id: Int,
    val teamId: Int,
    val userId: Int,
    val joinedAt: String
)
