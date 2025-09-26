package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.use_case.CreateChangeUseCase
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val createChangeUseCase: CreateChangeUseCase
) {
    suspend operator fun invoke(title: String): Task {
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title
        )
        localTaskRepository.createTask(task)

        val errorCode = remoteTaskRepository.createTask(task)

        if (errorCode != 0) {
            // save change
            createChangeUseCase(
                type = ChangeType.CREATE,
                objectId = task.id
            )
        }

        return task
    }
}