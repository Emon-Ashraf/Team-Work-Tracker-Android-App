package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTaskRepository
import com.example.teamworktracker.data.TaskRepository
import com.example.teamworktracker.domain.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MyTasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)

class MyTasksViewModel(
    private val repo: TaskRepository = RemoteTaskRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(MyTasksUiState())
    val state: StateFlow<MyTasksUiState> = _state

    fun loadMyTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val tasks = repo.getMyTasks()
                _state.value = _state.value.copy(isLoading = false, tasks = tasks)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load tasks"
                )
            }
        }
    }

    fun createTask(
        projectId: Int,
        assignedTo: Int,
        title: String,
        description: String,
        priority: String,
        status: String,
        dueDate: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                repo.createTask(
                    projectId = projectId,
                    assignedTo = assignedTo,
                    title = title,
                    description = description,
                    priority = priority,
                    status = status,
                    dueDate = dueDate
                )

                // refresh list so the new task appears in MyTasks
                val tasks = repo.getMyTasks()
                _state.value = _state.value.copy(isLoading = false, tasks = tasks)

                onSuccess()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create task"
                )
            }
        }
    }
}
