package com.tech.feature_sync_changes.domain.repository

import com.tech.feature_sync_changes.domain.model.Change

interface ChangeRepository {
    suspend fun createChange(change: Change): Change
    suspend fun getChanges(): List<Change>
    suspend fun deleteChange(id: Int)
}