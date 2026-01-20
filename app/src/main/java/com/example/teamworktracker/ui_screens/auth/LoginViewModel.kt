package com.example.teamworktracker.ui_screens.auth


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    var errorMessage = mutableStateOf<String?>(null)

    fun login(): Boolean {
        if (email.value.isBlank()) {
            errorMessage.value = "Email cannot be empty"
            return false
        }
        if (password.value.length < 6) {
            errorMessage.value = "Password must be at least 6 characters"
            return false
        }

        // Later: call real API here
        errorMessage.value = null
        return true
    }
}
