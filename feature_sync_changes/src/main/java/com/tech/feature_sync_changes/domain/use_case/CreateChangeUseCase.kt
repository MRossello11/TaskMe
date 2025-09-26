package com.tech.feature_sync_changes.domain.use_case

import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.model.Change
import com.tech.feature_sync_changes.domain.repository.ChangeRepository
import javax.inject.Inject

class CreateChangeUseCase @Inject constructor(
    private val changeRepository: ChangeRepository,
    private val clearChangesUseCase: ClearChangesUseCase
) {
    suspend operator fun invoke(type: ChangeType, objectId: String) {
        clearChangesUseCase(objectId)

        changeRepository.createChange(
            Change(
                type = type,
                objectId = objectId
            )
        )
    }
}