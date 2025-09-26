package com.tech.feature_sync_changes.domain.use_case

import com.tech.feature_sync_changes.domain.repository.ChangeRepository
import javax.inject.Inject

/**
 * This use case deletes all changes related to an object.
 * This is used when the object's state has been synced and there are no more
 * changes to record.
 */
class ClearChangesUseCase @Inject constructor(
    private val changeRepository: ChangeRepository
) {
    suspend operator fun invoke(objectId: String) {
        val changes = changeRepository.getChanges().filter { it.objectId == objectId }

        for (change in changes) {
            changeRepository.deleteChange(change.id)
        }
    }
}