package com.example.teamworktracker.network.api


import com.example.teamworktracker.domain.models.User
import retrofit2.http.GET

interface UsersApi {
    @GET("api/v1/users/me")
    suspend fun getMe(): User
}
