package com.tech.feature_tasks.domain.use_cases

import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.model.Sync
import com.tech.feature_sync_changes.domain.repository.ChangeRepository
import com.tech.feature_sync_changes.domain.repository.SyncRepository
import com.tech.feature_tasks.domain.repository.TaskRepository
import javax.inject.Inject

class SyncTasksUseCase @Inject constructor(
    @param:RoomTasks private val localTaskRepository: TaskRepository,
    @param:RetrofitTasks private val remoteTaskRepository: TaskRepository,
    private val changeRepository: ChangeRepository,
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke() {
        val localTasks = localTaskRepository.getTasks()

        val changes = changeRepository.getChanges()

        // sync deletions
        // delete remote tasks that are only locally flagged as deleted
        val localDeletedTasks = localTasks.filter { it.deleted }
        for (task in localDeletedTasks) {
            remoteTaskRepository.deleteTask(task.id)

            val change = changes.find { it.objectId == task.id }
            if (change != null) {
                changeRepository.deleteChange(change.id)
            }
        }

        // sync updates
        val localUpdatedTasks = localTasks.filter { task -> changes.find { change -> change.type == ChangeType.UPDATE && change.objectId == task.id } != null }
        for (task in localUpdatedTasks) {
            remoteTaskRepository.updateTask(task)

            val change = changes.find { it.objectId == task.id }
            if (change != null) {
                changeRepository.deleteChange(change.id)
            }
        }

        // sync creations
        val localCreatedTasks = localTasks.filter { task -> changes.find { change -> change.type == ChangeType.CREATE && change.objectId == task.id } != null }
        for (task in localCreatedTasks) {
            remoteTaskRepository.createTask(task)

            val change = changes.find { it.objectId == task.id }
            if (change != null) {
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