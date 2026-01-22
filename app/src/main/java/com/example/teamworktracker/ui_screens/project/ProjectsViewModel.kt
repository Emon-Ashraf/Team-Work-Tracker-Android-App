package com.example.teamworktracker.ui_screens.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.ProjectsRepository
import com.example.teamworktracker.domain.models.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
    val error: String? = null
)

class ProjectsViewModel(
    private val repo: ProjectsRepository = ProjectsRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectsUiState())
    val state: StateFlow<ProjectsUiState> = _state

    fun loadProjectsForTeam(teamId: Int) {
        viewModelScope.launch {
            _state.value = ProjectsUiState(isLoading = true)
            try {
                val all = repo.getAllProjects()
                val filtered = all.filter { it.teamId == teamId }
                _state.value = ProjectsUiState(projects = filtered)
            } catch (e: Exception) {
                _state.value = ProjectsUiState(error = e.message ?: "Failed to load projects")
            }
        }
    }

    fun createProject(teamId: Int, name: String, description: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repo.createProject(name, description, teamId)
                loadProjectsForTeam(teamId)
                onSuccess()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message ?: "Failed to create project")
            }
        }
    }
}
