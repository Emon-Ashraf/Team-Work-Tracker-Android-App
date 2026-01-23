package com.example.teamworktracker.ui_screens.auth

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamworktracker.core.session.SessionManager
import com.example.teamworktracker.data.AuthRepository
import com.example.teamworktracker.network.dto.LoginRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AuthRepository()
    private val session = SessionManager(app.applicationContext)

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)

    fun login() {
        viewModelScope.launch {
            _state.value = LoginUiState(loading = true)
            errorMessage.value = null

            try {
                val token = repo.login(LoginRequestDto(email = email.value, password = password.value))

                session.saveAccessToken(token.access_token)
                _state.value = LoginUiState(success = true)

            } catch (e: Exception) {
                _state.value = LoginUiState(error = e.message ?: "Login failed")
                errorMessage.value = e.message ?: "Login failed"
            }
        }
    }

    // Keeping the old login method for backward compatibility if needed, or remove if not used elsewhere
    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUiState(loading = true)

            try {
                val token = repo.login(LoginRequestDto(email = emailOrUsername, password = password))

                session.saveAccessToken(token.access_token)
                _state.value = LoginUiState(success = true)

            } catch (e: Exception) {
                _state.value = LoginUiState(error = e.message ?: "Login failed")
            }
        }
    }
}
