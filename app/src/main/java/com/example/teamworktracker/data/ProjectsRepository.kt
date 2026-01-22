package com.example.teamworktracker.data

import com.example.teamworktracker.domain.models.Project
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.network.api.CreateProjectRequest
import com.example.teamworktracker.network.api.ProjectsApi

class ProjectsRepository(
    private val api: ProjectsApi = ApiClient.retrofit().create(ProjectsApi::class.java)
) {

    suspend fun getAllProjects(): List<Project> = api.getAllProjects()

    suspend fun getProjectById(projectId: Int): Project = api.getProjectById(projectId)

    suspend fun createProject(name: String, description: String, teamId: Int): Project =
        api.createProject(CreateProjectRequest(name, description, team_id = teamId))
}
