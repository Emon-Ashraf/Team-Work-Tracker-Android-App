package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember

interface TeamRepository {
    suspend fun getMyTeams(): List<Team>
    suspend fun getAllTeams(skip: Int = 0, limit: Int = 100): List<Team>
    suspend fun getTeamById(teamId: Int): Team
    suspend fun createTeam(name: String, description: String): Team
    suspend fun joinTeam(teamId: Int): TeamMember
    suspend fun getMembersForTeam(teamId: Int): List<TeamMember>
}
