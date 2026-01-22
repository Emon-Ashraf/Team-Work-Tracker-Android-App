package com.example.teamworktracker.ui_screens.tasks

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.TasksApi
import com.example.teamworktracker.network.dto.TaskAttachmentLinkCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddAttachmentViewModel(
    private val api: TasksApi = ApiClient.retrofit().create(TasksApi::class.java)
) : ViewModel() {

    private val _state = MutableStateFlow(SimpleActionUiState())
    val state: StateFlow<SimpleActionUiState> = _state

    fun addLink(taskId: Int, url: String, description: String?) {
        viewModelScope.launch {
            _state.value = SimpleActionUiState(isLoading = true)
            try {
                api.addLinkAttachment(
                    taskId,
                    TaskAttachmentLinkCreateRequest(
                        description = description?.takeIf { it.isNotBlank() },
                        file_url = url
                    )
                )
                _state.value = SimpleActionUiState(done = true)
            } catch (e: Exception) {
                _state.value = SimpleActionUiState(error = e.message ?: "Failed to add link")
            }
        }
    }

    fun uploadFile(context: Context, taskId: Int, uri: Uri, description: String?) {
        viewModelScope.launch {
            _state.value = SimpleActionUiState(isLoading = true)
            try {
                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("Cannot read selected file")

                val mime = context.contentResolver.getType(uri) ?: "application/octet-stream"
                val reqBody = bytes.toRequestBody(mime.toMediaTypeOrNull())

                val filePart = MultipartBody.Part.createFormData(
                    name = "file",
                    filename = "upload_file",
                    body = reqBody
                )

                val descPart: RequestBody? =
                    description?.takeIf { it.isNotBlank() }?.toRequestBody("text/plain".toMediaTypeOrNull())

                api.uploadFileAttachment(taskId, filePart, descPart)

                _state.value = SimpleActionUiState(done = true)
            } catch (e: Exception) {
                _state.value = SimpleActionUiState(error = e.message ?: "Failed to upload file")
            }
        }
    }
}
