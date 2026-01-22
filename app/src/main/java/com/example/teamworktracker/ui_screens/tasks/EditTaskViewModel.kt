package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTaskRepository
import com.example.teamworktracker.data.TaskRepository
import com.example.teamworktracker.domain.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditTaskUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val task: Task? = null,
    val error: String? = null,
    val saved: Boolean = false
)

class EditTaskViewModel(
    private val repo: TaskRepository = RemoteTaskRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(EditTaskUiState())
    val state: StateFlow<EditTaskUiState> = _state

    fun load(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val task = repo.getTaskById(taskId)
                _state.value = _state.value.copy(isLoading = false, task = task)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message ?: "Failed to load task")
            }
        }
    }

    fun save(taskId: Int, body: Map<String, Any?>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null, saved = false)
            try {
                repo.updateTask(taskId, body)
                _state.value = _state.value.copy(isSaving = false, saved = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false, error = e.message ?: "Failed to update task")
            }
        }
    }
}
