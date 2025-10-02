package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.model.Sync
import com.tech.feature_sync_changes.domain.repository.ChangeRepository
import com.tech.feature_sync_changes.domain.repository.SyncRepository
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * This use case applies those changes that are not reflected remotely to ensure that local and
 * remote are in sync.
 * This is v1.0 of this use case where only 1 device is taken into account.
 * In future iterations multiple devices will be taken into account as well as some optimizations.
 */
class SyncTasksUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val changeRepository: ChangeRepository,
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke() {
        val localTasks = localTaskRepository.getTasks()

        val changes = changeRepository.getChanges()

        for (change in changes) {
            val task = localTasks.find { it.id == change.objectId }

            // change has no task assigned, delete change
            if (task == null) {
                changeRepository.deleteChange(change.id)
                continue
            }

            // apply the corresponding change
            val errorCode = when(change.type) {
                ChangeType.CREATE -> {
                    remoteTaskRepository.createTask(task)
                }
                ChangeType.UPDATE -> {
                    remoteTaskRepository.updateTask(task)
                }
                ChangeType.DELETE -> {
                    deleteTaskUseCase(task)
                }
            }

            if (errorCode == 0) {
                changeRepository.deleteChange(change.id)
            }
        }

        syncRepository.updateSync(
            Sync(
                lastSync = System.currentTimeMillis()
            )
        )
    }
}