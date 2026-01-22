package com.example.teamworktracker.network.api

import com.example.teamworktracker.network.dto.LoginRequestDto
import com.example.teamworktracker.network.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequestDto): TokenDto
}
