package com.tech.data.feature_sync_changes.repository

import com.tech.data.feature_sync_changes.entities.SyncEntity
import com.tech.data.feature_sync_changes.entities.SyncEntityDao
import com.tech.data.feature_sync_changes.entities.toDomain
import com.tech.feature_sync_changes.domain.model.Sync
import com.tech.feature_sync_changes.domain.repository.SyncRepository

class SyncRoomRepositoryImplementation(
    private val syncEntityDao: SyncEntityDao
): SyncRepository {
    override suspend fun updateSync(sync: Sync) {
        syncEntityDao.updateSync(sync.toEntity())
    }

    override suspend fun getSync(): Sync {
        return syncEntityDao.getSync().toDomain()
    }
}

fun Sync.toEntity() = SyncEntity(
    lastSync = lastSync
)