package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RoomTasks
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.use_case.CreateChangeUseCase
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class FlagDeleteTaskUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    private val createChangeUseCase: CreateChangeUseCase
) {
    suspend operator fun invoke(task: Task) {
        localTaskRepository.updateTask(task.copy(deleted = true))

        createChangeUseCase(
            type = ChangeType.DELETE,
            objectId = task.id
        )
    }
}