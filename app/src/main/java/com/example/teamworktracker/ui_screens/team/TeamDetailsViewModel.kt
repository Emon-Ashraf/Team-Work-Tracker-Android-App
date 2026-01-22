package com.example.teamworktracker.ui_screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTeamRepository
import com.example.teamworktracker.data.TeamRepository
import com.example.teamworktracker.domain.models.Team
import com.example.teamworktracker.domain.models.TeamMember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TeamDetailsUiState(
    val loading: Boolean = false,
    val team: Team? = null,
    val members: List<TeamMember> = emptyList(),
    val error: String? = null
)

class TeamDetailsViewModel(
    private val repo: TeamRepository = RemoteTeamRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailsUiState())
    val state: StateFlow<TeamDetailsUiState> = _state

    fun load(teamId: Int) {
        viewModelScope.launch {
            _state.value = TeamDetailsUiState(loading = true)
            try {
                val team = repo.getTeamById(teamId)
                val members = repo.getMembersForTeam(teamId)
                _state.value = TeamDetailsUiState(team = team, members = members)
            } catch (e: Exception) {
                _state.value = TeamDetailsUiState(error = e.message ?: "Failed to load team")
            }
        }
    }
}
