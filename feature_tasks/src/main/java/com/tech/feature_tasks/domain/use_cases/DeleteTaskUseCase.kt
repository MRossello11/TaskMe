package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.feature_sync_changes.domain.use_case.ClearChangesUseCase
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val flagDeleteTaskUseCase: FlagDeleteTaskUseCase,
    private val clearChangesUseCase: ClearChangesUseCase
) {
    suspend operator fun invoke(task: Task): Int {
        val errorCode = remoteTaskRepository.deleteTask(task.id)

        // if task was successfully deleted remotely, it can be safely deleted locally too
        if (errorCode == 0) {
            localTaskRepository.deleteTask(task.id)
            clearChangesUseCase(task.id)
        } else {
            flagDeleteTaskUseCase(task)
        }
        return errorCode
    }
}