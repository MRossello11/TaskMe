package com.tech.feature_sync_changes.domain.model

import com.tech.core.enums.ChangeType

/**
 * This class registers changes that have not been synced
 */
data class Change(
    val id: Int = 0,
    val type: ChangeType,
    val objectId: String,
    val timesTamp: Long = System.currentTimeMillis()
)
