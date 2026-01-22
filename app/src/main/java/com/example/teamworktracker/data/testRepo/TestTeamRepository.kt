package com.example.teamworktracker.data.testRepo


//for temporary
import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember

object TestTeamRepository {

    fun getMyTeams(): List<Team> {
        // shape similar to /api/v1/teams/my-teams
        return listOf(
            Team(
                id = 1,
                name = "El7ra2",
                description = "Main university project team",
                createdBy = 1,
                createdAt = "2025-01-01T10:00:00Z",
                updatedAt = "2025-01-10T12:00:00Z"
            ),
            Team(
                id = 2,
                name = "Bdany",
                description = "Side project team",
                createdBy = 2,
                createdAt = "2025-01-05T09:30:00Z",
                updatedAt = "2025-01-12T18:20:00Z"
            )
        )
    }

    fun getMembersForTeam(teamId: Int): List<TeamMember> {
        // shape similar to /api/v1/teams/{team_id}/members
        return listOf(
            TeamMember(
                id = 1,
                teamId = teamId,
                userId = 1,
                joinedAt = "2025-01-01T10:05:00Z"
            ),
            TeamMember(
                id = 2,
                teamId = teamId,
                userId = 2,
                joinedAt = "2025-01-02T11:00:00Z"
            ),
        )
    }
}
