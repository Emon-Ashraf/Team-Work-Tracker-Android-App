package com.example.teamworktracker.domain.models

import com.google.gson.annotations.SerializedName

enum class ProjectStatus {
    @SerializedName("planning")
    PLANNING,

    @SerializedName("in_progress")
    IN_PROGRESS,

    @SerializedName("completed")
    COMPLETED,

    @SerializedName("on_hold")
    ON_HOLD
}
