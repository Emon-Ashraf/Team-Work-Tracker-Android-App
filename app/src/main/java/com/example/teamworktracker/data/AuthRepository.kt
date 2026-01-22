package com.example.teamworktracker.data

import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.AuthApi
import com.example.teamworktracker.network.dto.LoginRequestDto
import com.example.teamworktracker.network.dto.TokenDto

class AuthRepository(
    private val api: AuthApi = ApiClient.retrofit().create(AuthApi::class.java)
) {
    suspend fun login(body: LoginRequestDto): TokenDto = api.login(body)
}
