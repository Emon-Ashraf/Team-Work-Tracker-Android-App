package com.example.teamworktracker.network.api

import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamsApi {

    // POST /api/v1/teams/
    @POST("api/v1/teams/")
    suspend fun createTeam(@Body body: CreateTeamRequest): Team

    // GET /api/v1/teams/ (all teams)
    @GET("api/v1/teams/")
    suspend fun getAllTeams(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Team>

    // GET /api/v1/teams/my-teams
    @GET("api/v1/teams/my-teams")
    suspend fun getMyTeams(): List<Team>

    // GET /api/v1/teams/{team_id}
    @GET("api/v1/teams/{team_id}")
    suspend fun getTeamById(@Path("team_id") teamId: Int): Team

    // POST /api/v1/teams/join  body: { "team_id": 0 }
    @POST("api/v1/teams/join")
    suspend fun joinTeam(@Body body: JoinTeamRequest): TeamMember

    // GET /api/v1/teams/{team_id}/members
    @GET("api/v1/teams/{team_id}/members")
    suspend fun getTeamMembers(@Path("team_id") teamId: Int): List<TeamMember>
}
