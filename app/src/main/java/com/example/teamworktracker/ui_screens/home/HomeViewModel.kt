package com.example.teamworktracker.ui_screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTaskRepository
import com.example.teamworktracker.data.TaskRepository
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskPriority
import com.example.teamworktracker.domain.models.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
) {
    val totalTasks: Int get() = tasks.size
    val newCount: Int get() = tasks.count { it.status == TaskStatus.NEW }
    val inProgressCount: Int get() = tasks.count { it.status == TaskStatus.IN_PROGRESS }
    val completedCount: Int get() = tasks.count { it.status == TaskStatus.COMPLETED }
    val highPriorityCount: Int get() = tasks.count { it.priority == TaskPriority.HIGH }

    val nextTasksPreview: List<Task>
        get() = tasks
            // ISO date strings sort correctly as strings
            .sortedBy { safeDueForSort(it.dueDate) }
            .take(3)
}

class HomeViewModel(
    private val repo: TaskRepository = RemoteTaskRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val tasks = repo.getMyTasks()
                _state.value = _state.value.copy(isLoading = false, tasks = tasks)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load dashboard"
                )
            }
        }
    }
}

private fun safeDueForSort(due: String): String {
    // push missing/invalid dates to bottom
    if (due.isBlank()) return "9999-12-31T23:59:59"
    return due
}
