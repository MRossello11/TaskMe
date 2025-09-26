package com.tech.feature_sync_changes.domain.repository

import com.tech.feature_sync_changes.domain.model.Sync

interface SyncRepository {
    suspend fun updateSync(sync: Sync)
    suspend fun getSync(): Sync
}