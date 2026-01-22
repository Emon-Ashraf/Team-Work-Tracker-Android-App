package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTaskRepository
import com.example.teamworktracker.data.TaskRepository
import com.example.teamworktracker.domain.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TaskDetailsUiState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val error: String? = null,
    val deleted: Boolean = false
)

class TaskDetailsViewModel(
    private val repo: TaskRepository = RemoteTaskRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailsUiState())
    val state: StateFlow<TaskDetailsUiState> = _state

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, deleted = false)
            try {
                val task = repo.getTaskById(taskId)
                _state.value = _state.value.copy(isLoading = false, task = task)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load task"
                )
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repo.deleteTask(taskId)
                _state.value = _state.value.copy(isLoading = false, deleted = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete task"
                )
            }
        }
    }
}
