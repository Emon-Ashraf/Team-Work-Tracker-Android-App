package com.example.teamworktracker.ui_screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    var email = mutableStateOf("")
    var username = mutableStateOf("")
    var fullName = mutableStateOf("")
    var password = mutableStateOf("")

    var errorMessage = mutableStateOf<String?>(null)

    fun register(): Boolean {
        if (email.value.isBlank()) {
            errorMessage.value = "Email cannot be empty"
            return false
        }
        if (username.value.isBlank()) {
            errorMessage.value = "Username cannot be empty"
            return false
        }
        if (fullName.value.isBlank()) {
            errorMessage.value = "Full name cannot be empty"
            return false
        }
        if (password.value.length < 6) {
            errorMessage.value = "Password must be at least 6 characters"
            return false
        }

        errorMessage.value = null
        return true
    }
}
