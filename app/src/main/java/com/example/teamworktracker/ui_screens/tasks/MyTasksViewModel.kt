package com.example.teamworktracker.ui_screens.tasks

import androidx.lifecycle.ViewModel
import com.example.teamworktracker.data.FakeTaskRepository
import com.example.teamworktracker.domain.models.Task

class MyTasksViewModel : ViewModel() {

    val tasks: List<Task> = FakeTaskRepository.getMyTasks()
}
