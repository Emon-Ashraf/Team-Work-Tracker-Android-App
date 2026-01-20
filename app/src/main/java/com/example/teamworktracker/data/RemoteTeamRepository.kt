package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.CreateTeamRequest
import com.example.teamworktracker.network.api.JoinTeamRequest
import com.example.teamworktracker.network.api.TeamsApi

class RemoteTeamRepository(
    private val api: TeamsApi = ApiClient.retrofit().create(TeamsApi::class.java)
) : TeamRepository {

    override suspend fun getMyTeams(): List<Team> = api.getMyTeams()

    override suspend fun getAllTeams(skip: Int, limit: Int): List<Team> =
        api.getAllTeams(skip, limit)

    override suspend fun getTeamById(teamId: Int): Team =
        api.getTeamById(teamId)

    override suspend fun createTeam(name: String, description: String): Team =
        api.createTeam(CreateTeamRequest(name, description))

    override suspend fun joinTeam(teamId: Int): TeamMember =
        api.joinTeam(JoinTeamRequest(team_id = teamId))

    override suspend fun getMembersForTeam(teamId: Int): List<TeamMember> =
        api.getTeamMembers(teamId)
}
