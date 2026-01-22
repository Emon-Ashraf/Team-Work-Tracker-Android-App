package com.example.teamworktracker.domain.models

data class User(
    val id: Int,
    val email: String,
    val full_name: String? = null
)
