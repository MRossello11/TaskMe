package com.tech.data.feature_sync_changes.repository

import com.tech.data.feature_sync_changes.entities.ChangeEntity
import com.tech.data.feature_sync_changes.entities.ChangeEntityDao
import com.tech.data.feature_sync_changes.entities.toDomain
import com.tech.feature_sync_changes.domain.model.Change
import com.tech.feature_sync_changes.domain.repository.ChangeRepository

class ChangeRoomRepositoryImplementation(
    private val changeEntityDao: ChangeEntityDao
): ChangeRepository {
    override suspend fun createChange(change: Change): Change {
        val id = changeEntityDao.insertChange(change.toEntity()).toInt()
        return change.copy(id = id)
    }

    override suspend fun getChanges(): List<Change> {
        return changeEntityDao.getChanges().map {
            it.toDomain()
        }
    }

    override suspend fun deleteChange(id: Int) {
        changeEntityDao.deleteChange(id)
    }
}

fun Change.toEntity() = ChangeEntity(
    id = id,
    type = type,
    objectId = objectId,
    timesTamp = timesTamp
)