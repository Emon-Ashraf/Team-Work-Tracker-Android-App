package com.example.teamworktracker.network.api

import com.example.teamworktracker.domain.models.Project
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class CreateProjectRequest(
    val name: String,
    val description: String,
    val team_id: Int
)

interface ProjectsApi {

    @POST("api/v1/projects/")
    suspend fun createProject(@Body body: CreateProjectRequest): Project

    @GET("api/v1/projects/")
    suspend fun getAllProjects(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Project>

    @GET("api/v1/projects/{project_id}")
    suspend fun getProjectById(@Path("project_id") projectId: Int): Project
}
