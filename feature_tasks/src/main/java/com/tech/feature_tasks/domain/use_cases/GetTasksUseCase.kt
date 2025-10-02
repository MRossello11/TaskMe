package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository
) {
    suspend operator fun invoke(): List<Task> {
        val remoteTasks = remoteTaskRepository.getTasks()

        return remoteTasks.ifEmpty {
            localTaskRepository.getTasks().filterNot { it.deleted }
        }
    }
}