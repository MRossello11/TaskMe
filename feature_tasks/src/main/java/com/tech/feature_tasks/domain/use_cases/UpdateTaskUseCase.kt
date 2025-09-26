package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.use_case.ClearChangesUseCase
import com.tech.feature_sync_changes.domain.use_case.CreateChangeUseCase
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val clearChangesUseCase: ClearChangesUseCase,
    private val createChangeUseCase: CreateChangeUseCase
) {
    suspend operator fun invoke(task: Task) {
        val errorCode = remoteTaskRepository.updateTask(task)

        localTaskRepository.updateTask(task)

        if (errorCode == 0) {
            clearChangesUseCase(task.id)
        } else {
            createChangeUseCase(
                type = ChangeType.UPDATE,
                objectId = task.id
            )
        }
    }
}