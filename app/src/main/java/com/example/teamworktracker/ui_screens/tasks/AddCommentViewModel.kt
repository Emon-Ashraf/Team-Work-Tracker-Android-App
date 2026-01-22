package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.TasksApi
import com.example.teamworktracker.network.dto.TaskCommentCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SimpleActionUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val done: Boolean = false
)

class AddCommentViewModel(
    private val api: TasksApi = ApiClient.retrofit().create(TasksApi::class.java)
) : ViewModel() {

    private val _state = MutableStateFlow(SimpleActionUiState())
    val state: StateFlow<SimpleActionUiState> = _state

    fun submit(taskId: Int, content: String) {
        viewModelScope.launch {
            _state.value = SimpleActionUiState(isLoading = true)
            try {
                api.addComment(taskId, TaskCommentCreateRequest(content = content))
                _state.value = SimpleActionUiState(done = true)
            } catch (e: Exception) {
                _state.value = SimpleActionUiState(error = e.message ?: "Failed to add comment")
            }
        }
    }
}
