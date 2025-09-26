package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.feature_sync_changes.domain.use_case.ClearChangesUseCase
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val clearChangesUseCase: ClearChangesUseCase
) {
    suspend operator fun invoke(): List<Task> {
        val localTasks = localTaskRepository.getTasks()
        val tasks = arrayListOf<Task>()
        tasks.addAll(localTasks.filterNot { it.deleted })

        val remoteTasks = remoteTaskRepository.getTasks()

        // check for tasks created or deleted remotely
        for (remoteTask in remoteTasks) {
            // check if task was created/updated remotely but not locally
            if (!remoteTask.deleted && localTasks.find { it -> it.id == remoteTask.id } == null) {
                tasks.add(remoteTask)
                localTaskRepository.createTask(remoteTask)
                clearChangesUseCase(remoteTask.id)
            }

            // check if task was deleted remotely but not locally
            if (remoteTask.deleted && localTasks.find { it -> it.id == remoteTask.id } != null) {
                tasks.removeIf { it -> it.id == remoteTask.id }
                localTaskRepository.deleteTask(remoteTask.id)
                clearChangesUseCase(remoteTask.id)
            }
        }

        return tasks
    }
}