package com.tech.data.feature_sync_changes.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import com.tech.feature_sync_changes.domain.model.Sync

@Entity
data class SyncEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val lastSync: Long
)

fun SyncEntity.toDomain() = Sync(
    lastSync = lastSync
)

@Dao
interface SyncEntityDao {
    @Insert(onConflict = REPLACE)
    suspend fun updateSync(syncEntity: SyncEntity)

    @Query("SELECT * FROM SyncEntity LIMIT 1")
    suspend fun getSync(): SyncEntity
}