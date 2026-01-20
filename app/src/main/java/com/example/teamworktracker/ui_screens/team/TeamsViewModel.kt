package com.example.teamworktracker.ui_screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTeamRepository
import com.example.teamworktracker.data.TeamRepository
import com.example.teamworktracker.domain.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TeamsUiState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val error: String? = null
)

class TeamsViewModel(
    private val repo: TeamRepository = RemoteTeamRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(TeamsUiState())
    val state: StateFlow<TeamsUiState> = _state

    fun loadMyTeams() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val teams = repo.getMyTeams()
                _state.value = _state.value.copy(isLoading = false, teams = teams)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load teams"
                )
            }
        }
    }
}
