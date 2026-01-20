package com.example.teamworktracker.ui_screens.project

import androidx.lifecycle.ViewModel
import com.example.teamworktracker.data.FakeProjectRepository
import com.example.teamworktracker.domain.models.Project

class ProjectsViewModel : ViewModel() {

    val myProjects: List<Project> = FakeProjectRepository.getMyProjects()
}
