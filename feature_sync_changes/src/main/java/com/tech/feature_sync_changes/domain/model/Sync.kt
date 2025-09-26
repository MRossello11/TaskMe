package com.tech.feature_sync_changes.domain.model

data class Sync(
    val id: Int = 0,
    val lastSync: Long // unix epoch time
)