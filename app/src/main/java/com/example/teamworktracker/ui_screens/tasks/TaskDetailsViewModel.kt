package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.data.RemoteTaskRepository
import com.example.teamworktracker.data.TaskRepository
import com.example.teamworktracker.domain.models.Task
import com.example.teamworktracker.domain.models.TaskAttachment
import com.example.teamworktracker.domain.models.TaskComment
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TaskDetailsUiState(
    val isLoading: Boolean = false,
    val task: Task? = null,

    val comments: List<TaskComment> = emptyList(),
    val attachments: List<TaskAttachment> = emptyList(),

    val error: String? = null,
    val deleted: Boolean = false
)

class TaskDetailsViewModel(
    private val repo: TaskRepository = RemoteTaskRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailsUiState())
    val state: StateFlow<TaskDetailsUiState> = _state

    fun loadAll(taskId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, deleted = false)

            try {
                // Load in parallel (faster UI)
                val taskDeferred = async { repo.getTaskById(taskId) }
                val commentsDeferred = async { repo.getTaskComments(taskId) }
                val attachmentsDeferred = async { repo.getTaskAttachments(taskId) }

                val task = taskDeferred.await()
                val comments = commentsDeferred.await()
                val attachments = attachmentsDeferred.await()

                _state.value = _state.value.copy(
                    isLoading = false,
                    task = task,
                    comments = comments,
                    attachments = attachments
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load task details"
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
